package com.terte.repository.payment;

import com.terte.common.enums.PaymentCreateType;
import com.terte.common.enums.PaymentMethod;
import com.terte.common.enums.PaymentStatus;
import com.terte.entity.payment.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class PaymentRepository {
    private final Map<Long, Payment> paymentStorage = new ConcurrentHashMap<>();

    PaymentRepository() {
        // 초기 데이터 설정
        Payment payment1 = new Payment(1L, 1L, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED,1L);
        paymentStorage.put(payment1.getId(), payment1);
    }

    public List<Payment> findByStoreId(Long storeId) {
        return paymentStorage.values().stream().filter(payment -> payment.getStoreId().equals(storeId)).collect(Collectors.toList());
    }

    public Payment findById(Long id) {
        return paymentStorage.get(id);
    }

    public Payment save(Payment payment) {
        if(payment.getId() == null) {
            payment.setId(paymentStorage.size() + 1L);
            paymentStorage.put(payment.getId(), payment);
        } else {
            paymentStorage.put(payment.getId(), payment);
        }
        return payment;
    }

    public void deleteById(Long id) {
        paymentStorage.remove(id);
    }
}
