package com.terte.service.payment;

import com.terte.common.enums.PaymentStatus;
import com.terte.entity.payment.Payment;

import com.terte.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    @Override
    public List<Payment> getAllPayments(Long storeId) {
        return paymentRepository.findByStoreId(storeId);
    }

    @Override
    public Payment getPaymentById(Long id) {
       return paymentRepository.findById(id);
    }

    @Override
    public Payment createPayment(Payment payment) {
        //TODO: 실제 결제가 진행되도록 로직 추가
        return paymentRepository.save(payment);
    }

    @Override
    public Payment cancelPayment(Long id) {
        Payment payment = paymentRepository.findById(id);
        payment.setStatus(PaymentStatus.PAYMENT_CANCELLED);
        //TODO: 실제 결제가 취소되도록 로직 추가
        return paymentRepository.save(payment);
    }
}
