package com.lumina.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        IN_ESCROW,
        COMPLETED,
        CANCELLED
    }

    private String id;
    private String hostId;
    private String hostName;
    private String hostEmail;
    private String vendorId;
    private String vendorName;
    private String serviceCategory;
    private LocalDate eventDate;
    private String location;
    private int guestCount;
    private double vendorFee;
    private double platformFee; // Lumina 10%
    private double totalAmount;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private String specialRequests;

    public Booking() {
        this.createdAt = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
    }

    public Booking(String id, String hostId, String hostName, String hostEmail,
                   String vendorId, String vendorName, String serviceCategory,
                   LocalDate eventDate, String location, int guestCount,
                   double vendorFee, String specialRequests) {
        this.id = id;
        this.hostId = hostId;
        this.hostName = hostName;
        this.hostEmail = hostEmail;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.serviceCategory = serviceCategory;
        this.eventDate = eventDate;
        this.location = location;
        this.guestCount = guestCount;
        this.vendorFee = vendorFee;
        this.platformFee = Math.round(vendorFee * 0.10 * 100.0) / 100.0;
        this.totalAmount = this.vendorFee + this.platformFee;
        this.status = BookingStatus.CONFIRMED;
        this.createdAt = LocalDateTime.now();
        this.specialRequests = specialRequests;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHostId() { return hostId; }
    public void setHostId(String hostId) { this.hostId = hostId; }

    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }

    public String getHostEmail() { return hostEmail; }
    public void setHostEmail(String hostEmail) { this.hostEmail = hostEmail; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getServiceCategory() { return serviceCategory; }
    public void setServiceCategory(String serviceCategory) { this.serviceCategory = serviceCategory; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getGuestCount() { return guestCount; }
    public void setGuestCount(int guestCount) { this.guestCount = guestCount; }

    public double getVendorFee() { return vendorFee; }
    public void setVendorFee(double vendorFee) {
        this.vendorFee = vendorFee;
        this.platformFee = Math.round(vendorFee * 0.10 * 100.0) / 100.0;
        this.totalAmount = this.vendorFee + this.platformFee;
    }

    public double getPlatformFee() { return platformFee; }
    public double getTotalAmount() { return totalAmount; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}

