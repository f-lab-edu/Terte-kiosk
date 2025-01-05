package com.terte.service.helper;

import com.terte.common.enums.OrderStatus;
import com.terte.dto.order.CreateOrderReqDTO;
import com.terte.dto.order.OrderItemDTO;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.Option;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import com.terte.service.menu.ChoiceService;
import com.terte.service.menu.MenuService;
import com.terte.service.menu.OptionService;
import com.terte.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceHelper {

    private final MenuService menuService;
    private final OptionService optionService;
    private final ChoiceService choiceService;

    public Order createOrder(CreateOrderReqDTO createOrderReqDTO, Long storeId) {
        List<OrderItem> orderItemList = createOrderItems(createOrderReqDTO.getOrderItemList());
        return new Order(null, storeId, OrderStatus.ORDERED, orderItemList,
                createOrderReqDTO.getOrderType(),
                createOrderReqDTO.getPhoneNumber(),
                createOrderReqDTO.getTableNumber());
    }

    private List<OrderItem> createOrderItems(List<OrderItemDTO> orderItemDTOList) {
        return orderItemDTOList.stream().map(orderItemDTO -> {
            Menu menu = menuService.getMenuById(orderItemDTO.getMenuId());
            List<SelectedOption> selectedOptionList = orderItemDTO.getSelectedOptions().stream().map(selectedOptionDTO -> {
                Option option = optionService.getOptionById(selectedOptionDTO.getOptionId());
                List<Choice> choices = selectedOptionDTO.getSelectedChoiceIds().stream().map(choiceService::getChoiceById).collect(Collectors.toList());
                return new SelectedOption(null, option, choices);
            }).collect(Collectors.toList());
            return new OrderItem(null, menu, orderItemDTO.getQuantity(), selectedOptionList);
        }).toList();
    }
}
