package com.terte.dto.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
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
public class UpdateOrderReqDTO{
    private Long id;
    private OrderStatus status;
    private List<MenuResDTO> menuList;
    private OrderType orderType;
    private String phoneNumber;
    private Integer tableNumber;
}
