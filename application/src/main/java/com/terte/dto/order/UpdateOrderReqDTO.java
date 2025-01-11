package com.terte.dto.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderReqDTO{
    @NotNull
    private Long id;
    private OrderStatus status;
    private OrderType orderType;
    private String phoneNumber;
    private Integer tableNumber;
}
