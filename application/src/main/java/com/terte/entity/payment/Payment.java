package com.terte.entity.payment;

import com.terte.common.enums.PaymentCreateType;
import com.terte.common.enums.PaymentMethod;
import com.terte.common.enums.PaymentStatus;
import com.terte.dto.order.CreateOrderReqDTO;
import com.terte.entity.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long storeId;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Long orderId;
}
