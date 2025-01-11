package com.terte.dto.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import com.terte.entity.order.Order;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderReqDTO {
    private List<OrderItemDTO> orderItemList;
    private OrderType orderType;
    private String phoneNumber;
    private Integer tableNumber;
    private Integer totalPrice;

}
