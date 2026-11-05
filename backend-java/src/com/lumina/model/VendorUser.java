package com.lumina.model;

import java.util.ArrayList;
import java.util.List;

public class VendorUser extends User {
    private static final long serialVersionUID = 1L;

    private String businessName;
    private String category; // Catering, Florals, Photography, Entertainment, Lighting, Venue
    private String city;
    private double basePrice;
    private double rating;
    private int reviewCount;
    private boolean verified;
    private List<String> portfolioImages;
    private double totalEarnings;

    public VendorUser() {
        super();
        this.role = "VENDOR";
        this.portfolioImages = new ArrayList<>();
        this.verified = true;
    }

    public VendorUser(String id, String fullName, String email, String phone,
                      String businessName, String category, String city, double basePrice) {
        super(id, fullName, email, phone, "VENDOR");
        this.businessName = businessName;
        this.category = category;
        this.city = city;
        this.basePrice = basePrice;
        this.rating = 4.9;
        this.reviewCount = 12;
        this.verified = true;
        this.portfolioImages = new ArrayList<>();
        this.totalEarnings = 0.0;
    }

    @Override
    public String getRoleDescription() {
        return "Lumina Verified Vendor: Providing top-tier " + category + " services with transparent escrow payments.";
    }

    // Getters and Setters
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public List<String> getPortfolioImages() { return portfolioImages; }
    public void setPortfolioImages(List<String> portfolioImages) { this.portfolioImages = portfolioImages; }

    public double getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(double totalEarnings) { this.totalEarnings = totalEarnings; }

    public void addEarnings(double amount) {
        this.totalEarnings += amount;
    }
}

