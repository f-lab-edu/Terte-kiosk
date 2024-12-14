package com.terte.dto.payment;

import com.terte.common.enums.PaymentMethod;
import com.terte.common.enums.PaymentStatus;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResDTO {
    private Long id;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
}
