package com.terte.service.payment;

import com.terte.entity.payment.Payment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    List<Payment> getAllPayments(Long storeId);
    Payment getPaymentById(Long id);
    Payment createPayment(Payment payment);
    Payment cancelPayment(Long id);
}
