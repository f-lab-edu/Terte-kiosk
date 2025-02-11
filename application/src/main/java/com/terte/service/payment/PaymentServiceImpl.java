package com.terte.service.payment;

import com.terte.common.enums.PaymentStatus;
import com.terte.common.exception.NotFoundException;
import com.terte.entity.payment.Payment;

import com.terte.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final Executor httpTaskExecutor;
    @Override
    public CompletableFuture<List<Payment>> getAllPayments(Long storeId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Payment> payments = paymentRepository.findByStoreId(storeId);
            if(payments.isEmpty()){
                throw new NotFoundException("Payment not found");
            }
            return payments;
        }, httpTaskExecutor);
    }

    @Override
    public CompletableFuture<Payment> getPaymentById(Long id) {
        return CompletableFuture.supplyAsync(() -> paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found")), httpTaskExecutor);
    }

    @Override
    public CompletableFuture<Payment> createPayment(Payment payment) {
        return CompletableFuture.supplyAsync(
                () -> paymentRepository.save(payment), httpTaskExecutor);
    }

    @Override
    public CompletableFuture<Payment> cancelPayment(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Payment payment = paymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Payment not found"));
            payment.setStatus(PaymentStatus.PAYMENT_CANCELLED);
            return paymentRepository.save(payment);
        }, httpTaskExecutor);
    }
}
