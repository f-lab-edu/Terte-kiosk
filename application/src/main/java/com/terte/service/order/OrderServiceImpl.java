package com.terte.service.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.exception.NotFoundException;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.MenuOption;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import com.terte.repository.order.OrderRepository;
import com.terte.service.menu.MenuService;
import com.terte.service.menu.OptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final MenuService menuService;
    @Override
    public List<Order> getAllOrders(Long storeId, OrderStatus status) {
        List<Order> orders;
        if(status == null){
            orders =  orderRepository.findByStoreId(storeId);
        }else{
            orders = orderRepository.findByStoreIdAndStatus(storeId,status);
        }
        if(orders.isEmpty()){
            throw new NotFoundException("Order not found");
        }
        return orders;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        Map<Long, Menu> menuCache = order.getOrderItems().stream()
                .map(item -> menuService.getMenuById(item.getMenuId()))
                .collect(Collectors.toMap(Menu::getId, menu -> menu));


        for (OrderItem item : order.getOrderItems()) {
            Menu menu = menuCache.get(item.getMenuId());

            for (MenuOption option : menu.getMenuOptions()) {
                if (option.getRequired()) {
                    boolean optionSelected = item.getSelectedOptions() != null &&
                            item.getSelectedOptions().stream()
                                    .anyMatch(selectedOption -> selectedOption.getMenuOptionId().equals(option.getId()));
                    if (!optionSelected) {
                        throw new IllegalArgumentException(
                                String.format("Required option '%s' not selected for menu '%s'", option.getName(), menu.getName())
                        );
                    }
                }
            }
        }

        //check multiple selection validation
        for (OrderItem item : order.getOrderItems()) {
            for (SelectedOption selectedOption : item.getSelectedOptions()) {
                Menu menu = menuCache.get(item.getMenuId());

                for(MenuOption option: menu.getMenuOptions()){
                    if(option.getId().equals(selectedOption.getMenuOptionId())){
                        if(!option.getMultipleSelection()){
                            long count = item.getSelectedOptions().stream()
                                    .filter(selectedOptionArg -> selectedOptionArg.getMenuOptionId().equals(option.getId()))
                                    .count();
                            if(count > 1){
                                throw new IllegalArgumentException(
                                        String.format("Multiple selection not allowed for option '%s' in menu '%s'", option.getName(), menu.getName())
                                );
                            }
                        }
                    }
                }
            }
        }
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order order) {
        Order existingOrder = orderRepository.findById(order.getId()).orElseThrow(() -> new NotFoundException("Order not found"));
        if(order.getOrderType() == null){
            order.setOrderType(existingOrder.getOrderType());
        }
        if(order.getPhoneNumber() == null){
            order.setPhoneNumber(existingOrder.getPhoneNumber());
        }
        if(order.getStatus() == null){
            order.setStatus(existingOrder.getStatus());
        }
        if(order.getTableNumber() == null){
            order.setTableNumber(existingOrder.getTableNumber());
        }
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        orderRepository.deleteById(id);

    }
}
