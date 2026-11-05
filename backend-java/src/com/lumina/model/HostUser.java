package com.lumina.model;

import java.util.ArrayList;
import java.util.List;

public class HostUser extends User {
    private static final long serialVersionUID = 1L;

    private String preferredCity;
    private double budgetRange;
    private List<String> bookingIds;

    public HostUser() {
        super();
        this.role = "HOST";
        this.bookingIds = new ArrayList<>();
    }

    public HostUser(String id, String fullName, String email, String phone, String preferredCity, double budgetRange) {
        super(id, fullName, email, phone, "HOST");
        this.preferredCity = preferredCity;
        this.budgetRange = budgetRange;
        this.bookingIds = new ArrayList<>();
    }

    @Override
    public String getRoleDescription() {
        return "Event Host: Curating luxury celebrations, booking verified elite vendors, and managing guest experiences.";
    }

    public String getPreferredCity() { return preferredCity; }
    public void setPreferredCity(String preferredCity) { this.preferredCity = preferredCity; }

    public double getBudgetRange() { return budgetRange; }
    public void setBudgetRange(double budgetRange) { this.budgetRange = budgetRange; }

    public List<String> getBookingIds() { return bookingIds; }
    public void setBookingIds(List<String> bookingIds) { this.bookingIds = bookingIds; }

    public void addBooking(String bookingId) {
        if (!this.bookingIds.contains(bookingId)) {
            this.bookingIds.add(bookingId);
        }
    }
}

