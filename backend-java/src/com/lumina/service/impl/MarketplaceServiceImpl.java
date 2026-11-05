package com.lumina.service.impl;

import com.lumina.concurrency.NotificationDispatcher;
import com.lumina.exception.BookingConflictException;
import com.lumina.exception.InvalidBookingException;
import com.lumina.exception.VendorNotFoundException;
import com.lumina.model.Booking;
import com.lumina.model.VendorUser;
import com.lumina.service.MarketplaceService;
import com.lumina.storage.FileDataManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MarketplaceServiceImpl implements MarketplaceService {
    private final FileDataManager dataManager = FileDataManager.getInstance();

    @Override
    public List<VendorUser> getAllVendors() {
        return new ArrayList<>(dataManager.getVendors().values());
    }

    @Override
    public VendorUser getVendorById(String id) throws VendorNotFoundException {
        VendorUser vendor = dataManager.getVendors().get(id);
        if (vendor == null) {
            throw new VendorNotFoundException("Vendor with ID '" + id + "' was not found in Lumina marketplace.");
        }
        return vendor;
    }

    @Override
    public List<VendorUser> searchVendors(String category, String city, Double maxBudget) {
        return dataManager.getVendors().values().stream()
                .filter(v -> category == null || category.equalsIgnoreCase("all") || v.getCategory().equalsIgnoreCase(category))
                .filter(v -> city == null || city.isEmpty() || v.getCity().equalsIgnoreCase(city))
                .filter(v -> maxBudget == null || v.getBasePrice() <= maxBudget)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized Booking createBooking(Booking booking)
            throws VendorNotFoundException, BookingConflictException, InvalidBookingException {

        // Validation (Software Testing Equivalence Partitioning / Boundary scenarios)
        if (booking.getVendorId() == null || booking.getVendorId().trim().isEmpty()) {
            throw new InvalidBookingException("Vendor ID must not be empty.");
        }
        if (booking.getGuestCount() <= 0 || booking.getGuestCount() > 5000) {
            throw new InvalidBookingException("Guest count must be between 1 and 5,000. Provided: " + booking.getGuestCount());
        }
        if (booking.getEventDate() == null || booking.getEventDate().isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Event date cannot be in the past.");
        }

        VendorUser vendor = dataManager.getVendors().get(booking.getVendorId());
        if (vendor == null) {
            throw new VendorNotFoundException("Cannot book: Vendor ID " + booking.getVendorId() + " does not exist.");
        }

        // Conflict Detection: Vendor cannot be double booked on same date unless status is CANCELLED
        boolean conflict = dataManager.getBookings().values().stream()
                .filter(b -> b.getVendorId().equals(booking.getVendorId()))
                .filter(b -> b.getStatus() != Booking.BookingStatus.CANCELLED)
                .anyMatch(b -> b.getEventDate().equals(booking.getEventDate()));

        if (conflict) {
            throw new BookingConflictException("Vendor '" + vendor.getBusinessName() +
                    "' is already booked for date " + booking.getEventDate() + ".");
        }

        // Assign ID and ensure vendor name
        if (booking.getId() == null || booking.getId().isEmpty()) {
            booking.setId("bk-" + UUID.randomUUID().toString().substring(0, 8));
        }
        booking.setVendorName(vendor.getBusinessName());
        booking.setServiceCategory(vendor.getCategory());

        // Lumina 10% Platform fee calculation (Slide 11 of Lumina Business presentation)
        if (booking.getVendorFee() <= 0) {
            booking.setVendorFee(vendor.getBasePrice());
        }

        dataManager.getBookings().put(booking.getId(), booking);
        dataManager.saveDataToDisk();

        // Multithreaded Asynchronous Notification
        NotificationDispatcher.dispatchAsync(
                booking.getHostEmail() != null ? booking.getHostEmail() : "host@lumina.in",
                "Your Lumina booking #" + booking.getId() + " with " + vendor.getBusinessName() + " has been initiated.",
                "BOOKING_CONFIRMATION"
        );
        NotificationDispatcher.dispatchAsync(
                vendor.getEmail(),
                "New Booking Alert! Host " + booking.getHostName() + " requested your services for " + booking.getEventDate(),
                "VENDOR_ALERT"
        );

        return booking;
    }

    @Override
    public List<Booking> getAllBookings() {
        return new ArrayList<>(dataManager.getBookings().values());
    }

    @Override
    public Booking getBookingById(String id) {
        return dataManager.getBookings().get(id);
    }

    @Override
    public List<Booking> getBookingsByHost(String hostId) {
        return dataManager.getBookings().values().stream()
                .filter(b -> b.getHostId() != null && b.getHostId().equals(hostId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsByVendor(String vendorId) {
        return dataManager.getBookings().values().stream()
                .filter(b -> b.getVendorId() != null && b.getVendorId().equals(vendorId))
                .collect(Collectors.toList());
    }

    @Override
    public synchronized Booking updateBookingStatus(String bookingId, Booking.BookingStatus status) {
        Booking b = dataManager.getBookings().get(bookingId);
        if (b != null) {
            b.setStatus(status);
            dataManager.saveDataToDisk();
            NotificationDispatcher.dispatchAsync(
                    b.getHostEmail(),
                    "Your booking #" + b.getId() + " status is now: " + status,
                    "STATUS_CHANGE"
            );
        }
        return b;
    }
}

