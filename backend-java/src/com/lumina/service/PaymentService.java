package com.lumina.service;

import com.lumina.model.Payment;
import com.lumina.exception.PaymentFailedException;

public interface PaymentService {
    Payment processEscrowPayment(String bookingId, double amount, String paymentMethod) throws PaymentFailedException;
    Payment releaseEscrow(String transactionId) throws PaymentFailedException;
    Payment refundPayment(String transactionId) throws PaymentFailedException;
    Payment getPaymentByBooking(String bookingId);
}

