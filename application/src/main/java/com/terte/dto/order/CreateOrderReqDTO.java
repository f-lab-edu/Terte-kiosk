package com.terte.dto.order;

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
public class CreateOrderReqDTO {
    private List<MenuResDTO> menuList;
    private OrderType orderType;
    private String phoneNumber;
    private Integer tableNumber;
}
