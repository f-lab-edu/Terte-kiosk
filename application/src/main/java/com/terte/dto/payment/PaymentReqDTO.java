package com.terte.dto.payment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.terte.common.enums.PaymentCreateType;
import com.terte.common.enums.PaymentMethod;
import com.terte.dto.order.CreateOrderReqDTO;
import com.terte.dto.order.OrderDetailResDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentReqDTO {
    private PaymentCreateType paymentCreateType;
    private PaymentMethod paymentMethod;
    @Min(0)
    private Long orderId;
    private CreateOrderReqDTO order;


}
