package com.terte.dto.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import com.terte.dto.menu.MenuResDTO;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResDTO {
    private Long id;
    private List<OrderItemDTO> orderItemList;
    private OrderType orderType;
    private OrderStatus status;
    private String orderTime;
    private int totalPrice;
}
