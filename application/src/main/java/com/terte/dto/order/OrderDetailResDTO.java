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
public class OrderDetailResDTO {
    private Long orderId;
    private List<OrderItemDTO> orderItemList;
    private OrderType orderType;
    private OrderStatus status;
    private String orderTime;
    private String phoneNumber;
    private Integer tableNumber;
    private Long totalPrice;

    public static OrderDetailResDTO from(Order order){
        List<OrderItemDTO> orderItemList = order.getOrderItems().stream().map(OrderItemDTO::from).collect(Collectors.toList());
        return new OrderDetailResDTO(order.getId(), orderItemList, order.getOrderType(), order.getStatus(), order.getCreateTime().toString(),order.getPhoneNumber(),order.getTableNumber() ,order.getTotalPrice());
    }
}
