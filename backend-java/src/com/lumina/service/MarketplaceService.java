package com.lumina.service;

import com.lumina.model.Booking;
import com.lumina.model.VendorUser;
import com.lumina.exception.VendorNotFoundException;
import com.lumina.exception.BookingConflictException;
import com.lumina.exception.InvalidBookingException;

import java.util.List;

public interface MarketplaceService {
    List<VendorUser> getAllVendors();
    VendorUser getVendorById(String id) throws VendorNotFoundException;
    List<VendorUser> searchVendors(String category, String city, Double maxBudget);
    
    Booking createBooking(Booking booking) throws VendorNotFoundException, BookingConflictException, InvalidBookingException;
    List<Booking> getAllBookings();
    Booking getBookingById(String id);
    List<Booking> getBookingsByHost(String hostId);
    List<Booking> getBookingsByVendor(String vendorId);
    Booking updateBookingStatus(String bookingId, Booking.BookingStatus status);
}

