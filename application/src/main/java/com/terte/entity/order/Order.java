package com.terte.entity.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    private Long id;
    private Long storeId;
    private OrderStatus status;
    private List<OrderItem> orderItemList;
    private OrderType orderType;
    private String phoneNumber;
    private Integer tableNumber;
}
