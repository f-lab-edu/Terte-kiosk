package com.terte.service.order;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.menu.MenuOption;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import com.terte.repository.menu.OptionRepository;
import com.terte.repository.order.OrderRepository;
import com.terte.service.menu.OptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OptionService optionService;
    @Override
    public List<Order> getAllOrders(Long storeId) {
        List<Order> orders = orderRepository.findByStoreId(storeId);
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
        // Check if all required options are selected
        for (OrderItem item : order.getOrderItems()) {
            for (SelectedOption selectedOption : item.getSelectedOptions()) {
                MenuOption option = optionService.getOptionById(selectedOption.getMenuOptionId());
                if (option.getRequired() && selectedOption.getSelectedChoiceIds().isEmpty()) {
                    throw new NotFoundException("Required option not selected");
                }
            }
        }

        //check multiple selection validation
        for (OrderItem item : order.getOrderItems()) {
            for (SelectedOption selectedOption : item.getSelectedOptions()) {
                MenuOption option = optionService.getOptionById(selectedOption.getMenuOptionId());
                if (!option.getMultipleSelection() && selectedOption.getSelectedChoiceIds().size() > 1) {
                    throw new NotFoundException("Multiple selection not allowed");
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
