package com.terte.service.payment;

import com.terte.common.enums.PaymentStatus;
import com.terte.common.exception.NotFoundException;
import com.terte.entity.payment.Payment;

import com.terte.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    @Override
    @Async
    public CompletableFuture<List<Payment>> getAllPayments(Long storeId) {
        return CompletableFuture.supplyAsync(()->{
            List<Payment> payments = paymentRepository.findByStoreId(storeId);
            if(payments.isEmpty()){
                throw new NotFoundException("Payment not found");
            }
            return payments;
        });
    }

    @Override
    @Async
    public CompletableFuture<Payment> getPaymentById(Long id) {
        return CompletableFuture.supplyAsync(()->{
            return paymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Payment not found"));
        });
    }

    @Override
    @Async
    public CompletableFuture<Payment> createPayment(Payment payment) {
        //TODO: 실제 결제가 진행되도록 로직 추가
        return CompletableFuture.supplyAsync(()->{
            return paymentRepository.save(payment);
        });
    }

    @Override
    @Async
    public CompletableFuture<Payment> cancelPayment(Long id) {
        return CompletableFuture.supplyAsync(()->{
            Payment payment = paymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Payment not found"));
            payment.setStatus(PaymentStatus.PAYMENT_CANCELLED);
                    return paymentRepository.save(payment);
        });
    }
}
