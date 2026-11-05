package com.lumina.service.impl;

import com.lumina.concurrency.NotificationDispatcher;
import com.lumina.exception.PaymentFailedException;
import com.lumina.model.Booking;
import com.lumina.model.Payment;
import com.lumina.model.VendorUser;
import com.lumina.service.PaymentService;
import com.lumina.storage.FileDataManager;

import java.util.UUID;

public class PaymentServiceImpl implements PaymentService {
    private final FileDataManager dataManager = FileDataManager.getInstance();

    @Override
    public synchronized Payment processEscrowPayment(String bookingId, double amount, String paymentMethod)
            throws PaymentFailedException {

        Booking booking = dataManager.getBookings().get(bookingId);
        if (booking == null) {
            throw new PaymentFailedException("Booking #" + bookingId + " not found. Cannot initiate escrow payment.");
        }

        if (amount <= 0) {
            throw new PaymentFailedException("Invalid transaction amount: ₹" + amount);
        }

        // Simulate sandbox gateway verification (Test mode only)
        if (paymentMethod != null && paymentMethod.toUpperCase().contains("FAIL")) {
            throw new PaymentFailedException("Payment gateway declined transaction (Sandbox Test Triggered).");
        }

        String txId = "tx-" + UUID.randomUUID().toString().substring(0, 8);
        Payment payment = new Payment(txId, bookingId, booking.getVendorFee(), paymentMethod);
        payment.setStatus(Payment.PaymentStatus.HELD_IN_ESCROW);

        // Update booking state
        booking.setStatus(Booking.BookingStatus.IN_ESCROW);

        dataManager.getPayments().put(txId, payment);
        dataManager.saveDataToDisk();

        // Background dispatch
        NotificationDispatcher.dispatchAsync(
                booking.getHostEmail(),
                "Payment of ₹" + payment.getGrossAmount() + " secured in Lumina Escrow (Tx ID: " + txId + ").",
                "ESCROW_FUNDED"
        );

        return payment;
    }

    @Override
    public synchronized Payment releaseEscrow(String transactionId) throws PaymentFailedException {
        Payment payment = dataManager.getPayments().get(transactionId);
        if (payment == null) {
            throw new PaymentFailedException("Transaction ID '" + transactionId + "' does not exist.");
        }

        if (payment.getStatus() != Payment.PaymentStatus.HELD_IN_ESCROW) {
            throw new PaymentFailedException("Cannot release escrow: current status is " + payment.getStatus());
        }

        payment.setStatus(Payment.PaymentStatus.RELEASED_TO_VENDOR);

        Booking booking = dataManager.getBookings().get(payment.getBookingId());
        if (booking != null) {
            booking.setStatus(Booking.BookingStatus.COMPLETED);
            VendorUser vendor = dataManager.getVendors().get(booking.getVendorId());
            if (vendor != null) {
                vendor.addEarnings(payment.getVendorAmount());
            }
        }

        dataManager.saveDataToDisk();

        NotificationDispatcher.dispatchAsync(
                booking != null ? booking.getHostEmail() : "host@lumina.in",
                "Escrow funds ₹" + payment.getVendorAmount() + " released to vendor following verified event completion.",
                "PAYMENT_RELEASED"
        );

        return payment;
    }

    @Override
    public synchronized Payment refundPayment(String transactionId) throws PaymentFailedException {
        Payment payment = dataManager.getPayments().get(transactionId);
        if (payment == null) {
            throw new PaymentFailedException("Transaction ID '" + transactionId + "' does not exist.");
        }
        payment.setStatus(Payment.PaymentStatus.REFUNDED);

        Booking booking = dataManager.getBookings().get(payment.getBookingId());
        if (booking != null) {
            booking.setStatus(Booking.BookingStatus.CANCELLED);
        }

        dataManager.saveDataToDisk();
        return payment;
    }

    @Override
    public Payment getPaymentByBooking(String bookingId) {
        return dataManager.getPayments().values().stream()
                .filter(p -> p.getBookingId() != null && p.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }
}

