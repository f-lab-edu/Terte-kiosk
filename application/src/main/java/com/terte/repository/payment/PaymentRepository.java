package com.terte.repository.payment;

import com.terte.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<List<Payment>> findByStoreId(Long storeId);
}
