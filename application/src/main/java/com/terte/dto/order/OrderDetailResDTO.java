package com.terte.dto.order;

import com.terte.common.OrderStatus;
import com.terte.common.OrderType;
import com.terte.dto.menu.MenuResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResDTO {
    private Long orderId;
    private List<MenuResDTO> menuList;
    private OrderType orderType;
    private OrderStatus status;
    private String orderTime;
    private String phoneNumber;
    private Integer tableNumber;
}
