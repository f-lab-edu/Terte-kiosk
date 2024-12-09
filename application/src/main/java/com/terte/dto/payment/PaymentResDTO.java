package com.terte.dto.payment;

import com.terte.common.enums.PaymentMethod;
import com.terte.common.enums.PaymentStatus;
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
