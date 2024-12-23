package com.terte.dto.order;

import com.terte.common.enums.OrderType;
import com.terte.dto.menu.MenuResDTO;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderReqDTO {
    private List<MenuResDTO> menuList;
    private OrderType orderType;
    private String phoneNumber;
    private Integer tableNumber;
    private Integer totalPrice;
}
