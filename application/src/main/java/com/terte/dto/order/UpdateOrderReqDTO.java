package com.terte.dto.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import com.terte.dto.menu.MenuResDTO;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderReqDTO{
    private Long id;
    private OrderStatus status;
    private List<MenuResDTO> menuList;
    private OrderType orderType;
    private String phoneNumber;
    private Integer tableNumber;
}
