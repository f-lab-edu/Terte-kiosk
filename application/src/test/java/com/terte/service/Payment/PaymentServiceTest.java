package com.terte.service.Payment;

import com.terte.common.enums.PaymentStatus;
import com.terte.common.enums.PaymentMethod;
import com.terte.common.exception.NotFoundException;
import com.terte.entity.payment.Payment;
import com.terte.repository.payment.PaymentRepository;
import com.terte.service.payment.PaymentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Test
    @DisplayName("모든 결제 조회")
    void getAllPayments() {
        Long storeId = 1L;
        List<Payment> paymentList = List.of(new Payment(1L, storeId, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, 1L, 1000L), new Payment(2L, storeId, PaymentMethod.CREDIT_CARD, PaymentStatus.PAYMENT_COMPLETED, 2L, 1000L));
        when(paymentRepository.findByStoreId(storeId)).thenReturn(paymentList);

        List<Payment> result = paymentService.getAllPayments(storeId);

        assertEquals(2, result.size());
        verify(paymentRepository,times(1)).findByStoreId(storeId);
    }

    @Test
    @DisplayName("결제 ID로 결제 조회")
    void getPaymentById() {
        Long id = 1L;
        Payment payment = new Payment(id, 1L, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, 1L, 1000L);
        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));

        Payment result = paymentService.getPaymentById(id);

        assertEquals(id, result.getId());
        verify(paymentRepository,times(1)).findById(id);
    }

    @Test
    @DisplayName("결제 생성")
    void createPayment() {
        Payment payment = new Payment(1L, 1L, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, 1L, 1000L);
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.createPayment(payment);

        assertEquals(payment, result);
        verify(paymentRepository,times(1)).save(payment);
    }

    @Test
    @DisplayName("결제 취소")
    void cancelPayment() {
        Long id = 1L;
        Payment payment = new Payment(id, 1L, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, 1L, 1000L);
        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.cancelPayment(id);

        assertEquals(PaymentStatus.PAYMENT_CANCELLED, result.getStatus());
        verify(paymentRepository,times(1)).findById(id);
        verify(paymentRepository,times(1)).save(payment);
    }

    @Test
    @DisplayName("존재하지 않는 결제 취소")
    void cancelPaymentNotFound() {
        Long id = 1L;
        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.cancelPayment(id));
        verify(paymentRepository,times(1)).findById(id);
    }


}
