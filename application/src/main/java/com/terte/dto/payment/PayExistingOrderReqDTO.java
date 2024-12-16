package com.terte.dto.payment;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PayExistingOrderReqDTO extends PaymentReqDTO{
    private Long orderId;
}
