package com.terte.dto.payment;

import com.terte.common.enums.PaymentMethod;
import com.terte.common.enums.PaymentStatus;
import com.terte.entity.payment.Payment;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResDTO {
    private Long id;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;

    public static PaymentResDTO from(Payment payment) {
        return new PaymentResDTO(payment.getId(), payment.getOrder().getId(), payment.getPaymentMethod(), payment.getStatus());
    }
}
