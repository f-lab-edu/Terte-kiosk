package com.terte.dto.payment;

import com.terte.common.PaymentCreateType;
import com.terte.common.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReqDTO {
    private PaymentCreateType paymentCreateType;
    private PaymentMethod paymentMethod;
    private Integer amount;
}
