package com.lumina.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum PaymentStatus {
        INITIATED,
        HELD_IN_ESCROW,
        RELEASED_TO_VENDOR,
        REFUNDED,
        FAILED
    }

    private String transactionId;
    private String bookingId;
    private double grossAmount;
    private double vendorAmount;
    private double platformFee;
    private String paymentMethod; // "CARD_TEST", "UPI_SANDBOX", "NETBANKING_TEST"
    private PaymentStatus status;
    private LocalDateTime timestamp;
    private boolean sandboxMode;

    public Payment() {
        this.timestamp = LocalDateTime.now();
        this.sandboxMode = true;
    }

    public Payment(String transactionId, String bookingId, double vendorAmount, String paymentMethod) {
        this.transactionId = transactionId;
        this.bookingId = bookingId;
        this.vendorAmount = vendorAmount;
        this.platformFee = Math.round(vendorAmount * 0.10 * 100.0) / 100.0;
        this.grossAmount = this.vendorAmount + this.platformFee;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.HELD_IN_ESCROW;
        this.timestamp = LocalDateTime.now();
        this.sandboxMode = true; // Always true for integrated assignment
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public double getGrossAmount() { return grossAmount; }
    public void setGrossAmount(double grossAmount) { this.grossAmount = grossAmount; }

    public double getVendorAmount() { return vendorAmount; }
    public void setVendorAmount(double vendorAmount) { this.vendorAmount = vendorAmount; }

    public double getPlatformFee() { return platformFee; }
    public void setPlatformFee(double platformFee) { this.platformFee = platformFee; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isSandboxMode() { return sandboxMode; }
    public void setSandboxMode(boolean sandboxMode) { this.sandboxMode = sandboxMode; }
}

