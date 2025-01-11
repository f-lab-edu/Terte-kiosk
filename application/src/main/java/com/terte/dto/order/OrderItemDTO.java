package com.terte.dto.order;

import com.terte.entity.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private Long menuId;
    private Integer quantity;
    private List<SelectedOptionDTO> selectedOptions;

}