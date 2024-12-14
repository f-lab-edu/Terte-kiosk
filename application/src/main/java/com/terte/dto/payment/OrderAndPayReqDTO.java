package com.terte.dto.payment;

import com.terte.dto.order.CreateOrderReqDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrderAndPayReqDTO extends PaymentReqDTO{
    private CreateOrderReqDTO order;
}
