package com.terte.dto.payment;

import com.terte.dto.order.CreateOrderReqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAndPayReqDTO extends PaymentReqDTO{
    private CreateOrderReqDTO order;
}
