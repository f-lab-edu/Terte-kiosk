package com.terte.service.payment;

import com.terte.entity.payment.Payment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface PaymentService {
    CompletableFuture<List<Payment>> getAllPayments(Long storeId);
    CompletableFuture<Payment> getPaymentById(Long id);
    CompletableFuture<Payment> createPayment(Payment payment);
    CompletableFuture<Payment> cancelPayment(Long id);
}
