package com.terte.dto.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import com.terte.entity.order.Order;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResDTO {
    private Long id;
    private List<OrderItemDTO> orderItemList;
    private OrderType orderType;
    private OrderStatus status;
    private String orderTime;
    private Long totalPrice;

    public static OrderResDTO from(Order order){
        List<OrderItemDTO> orderItemList = order.getOrderItems().stream().map(OrderItemDTO::from).collect(Collectors.toList());
        return new OrderResDTO(order.getId(), orderItemList, order.getOrderType(), order.getStatus(), order.getCreateTime().toString(), order.getTotalPrice());
    }
}
