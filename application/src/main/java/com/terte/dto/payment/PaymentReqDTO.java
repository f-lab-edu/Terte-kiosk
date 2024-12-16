package com.terte.dto.payment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.terte.common.enums.PaymentCreateType;
import com.terte.common.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "paymentCreateType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PayExistingOrderReqDTO.class, name = "PAY_EXISTING_ORDER"),
        @JsonSubTypes.Type(value = OrderAndPayReqDTO.class, name = "ORDER_AND_PAY")
})
public class PaymentReqDTO {
    private PaymentCreateType paymentCreateType;
    private PaymentMethod paymentMethod;
}
