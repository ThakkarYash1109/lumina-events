package com.lumina.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Review implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String bookingId;
    private String vendorId;
    private String hostName;
    private double rating;
    private String comment;
    private boolean verifiedStayOrBooking; // Fake review prevention
    private LocalDateTime createdAt;

    public Review() {
        this.createdAt = LocalDateTime.now();
        this.verifiedStayOrBooking = true;
    }

    public Review(String id, String bookingId, String vendorId, String hostName, double rating, String comment) {
        this.id = id;
        this.bookingId = bookingId;
        this.vendorId = vendorId;
        this.hostName = hostName;
        this.rating = rating;
        this.comment = comment;
        this.verifiedStayOrBooking = (bookingId != null && !bookingId.trim().isEmpty());
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }

    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public boolean isVerifiedStayOrBooking() { return verifiedStayOrBooking; }
    public void setVerifiedStayOrBooking(boolean verifiedStayOrBooking) { this.verifiedStayOrBooking = verifiedStayOrBooking; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

