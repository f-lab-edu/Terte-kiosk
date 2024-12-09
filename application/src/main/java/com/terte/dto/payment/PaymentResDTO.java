package com.terte.dto.payment;

import com.terte.common.PaymentMethod;
import com.terte.common.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResDTO {
    private Long id;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private Integer amount;
    private PaymentStatus status;
}
