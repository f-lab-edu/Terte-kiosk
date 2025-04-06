package com.terte.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventDTO {
    private Long storeId;
    private List<OrderItemDTO> orderItemDTOList;
}
