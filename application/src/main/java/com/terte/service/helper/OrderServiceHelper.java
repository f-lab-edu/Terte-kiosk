package com.terte.service.helper;

import com.terte.common.enums.OrderStatus;
import com.terte.dto.order.CreateOrderReqDTO;
import com.terte.dto.order.OrderItemDTO;
import com.terte.dto.order.SelectedOptionDTO;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.MenuOption;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import com.terte.service.menu.ChoiceService;
import com.terte.service.menu.MenuService;
import com.terte.service.menu.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class OrderServiceHelper {

    private final MenuService menuService;
    private final OptionService optionService;
    private final ChoiceService choiceService;

    public Order createOrder(CreateOrderReqDTO createOrderReqDTO, Long storeId) {
        Order order = new Order();
        order.setStoreId(storeId);
        order.setStatus(OrderStatus.ORDERED);
        order.setOrderType(createOrderReqDTO.getOrderType());
        order.setPhoneNumber(createOrderReqDTO.getPhoneNumber());
        order.setTableNumber(createOrderReqDTO.getTableNumber());




        createOrderReqDTO.getOrderItemList().forEach(orderItemDTO -> {
            OrderItem orderItem = createOrderItem(orderItemDTO);
            order.addOrderItem(orderItem);
        });

        long price = 0;

        for (OrderItem orderItem : order.getOrderItems()) {
            Menu menu = menuService.getMenuById(orderItem.getMenuId());
            price += (long) menu.getPrice() * orderItem.getQuantity();

            for (SelectedOption selectedOption : orderItem.getSelectedOptions()) {
                MenuOption menuOption = optionService.getOptionById(selectedOption.getMenuOptionId());
                for (Choice choice : menuOption.getChoices()) {
                    price += choice.getPrice();
                }
            }
        }

        order.setTotalPrice(price);


        return order;
    }

    private OrderItem createOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        isMenuExist(orderItemDTO.getMenuId());
        orderItem.setMenuId(orderItemDTO.getMenuId());
        orderItem.setQuantity(orderItemDTO.getQuantity());

        orderItemDTO.getSelectedOptions().forEach(selectedOptionDTO -> {
            SelectedOption selectedOption = createSelectedOption(selectedOptionDTO);
            orderItem.addSelectedOption(selectedOption);
        });

        return orderItem;
    }

    private SelectedOption createSelectedOption(SelectedOptionDTO selectedOptionDTO) {
        isOptionExist(selectedOptionDTO.getOptionId());
        isChoiceExist(selectedOptionDTO.getSelectedChoiceIds());

        SelectedOption selectedOption = new SelectedOption();
        selectedOption.setMenuOptionId(selectedOptionDTO.getOptionId());
        selectedOption.setSelectedChoiceIds(selectedOptionDTO.getSelectedChoiceIds());

        return selectedOption;
    }

    private void isChoiceExist(List<Long> choiceIds) {
        choiceIds.forEach(choiceService::getChoiceById);
    }

    private void isOptionExist(Long optionId) {
        optionService.getOptionById(optionId);
    }

    private void isMenuExist(Long menuId) {
       menuService.getMenuById(menuId);
    }

}
