package com.terte.service.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.exception.NotFoundException;
import com.terte.dto.order.OrderEventDTO;
import com.terte.dto.order.OrderItemDTO;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.MenuOption;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import com.terte.event.OrderEventProducer;
import com.terte.repository.order.OrderRepository;
import com.terte.service.menu.MenuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final Executor httpTaskExecutor;
    private final OrderEventProducer orderEventProducer;
    @Override
    @Async("httpTaskExecutor")
    public CompletableFuture<List<Order>> getAllOrders(Long storeId, OrderStatus status) {
        return CompletableFuture.supplyAsync(() -> {
            List<Order> orders;

            if (status == null) {
                orders = orderRepository.findByStoreId(storeId);
            } else {
                orders = orderRepository.findByStoreIdAndStatus(storeId, status);
            }

            if (orders.isEmpty()) {
                throw new NotFoundException("Order not found");
            }

            return orders;
        }, httpTaskExecutor);
    }

    @Override
    public CompletableFuture<Order> getOrderById(Long id) {
        return CompletableFuture.supplyAsync(() ->
                        orderRepository.findById(id)
                                .orElseThrow(() -> new NotFoundException("Order not found"))
                , httpTaskExecutor);
    }



    @Override
    @Transactional
    public CompletableFuture<Order> createOrder(Order order) {
        return CompletableFuture.supplyAsync(() -> {

            Map<Long, Menu> menuCache = menuService.getMenuByids(order.getOrderItems().stream().map(OrderItem::getMenuId).collect(Collectors.toList()))
                    .stream().collect(Collectors.toMap(Menu::getId, menu -> menu));


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
                                        .filter(selectedOption1 -> selectedOption1.getSelectedChoiceIds().size() > 1).count();
                                if(count > 0){
                                    throw new IllegalArgumentException(
                                            String.format("Multiple selection not allowed for option '%s' in menu '%s'", option.getName(), menu.getName())
                                    );
                                }
                            }
                        }
                    }
                }
            }

            //카프카 이벤트 발행
            OrderEventDTO orderEvent = new OrderEventDTO(order.getStoreId(), order.getOrderItems().stream().map(OrderItemDTO::from).collect(Collectors.toList()));
            orderEventProducer.sendOrderEvent(orderEvent);

            return orderRepository.save(order);
        }, httpTaskExecutor);


    }

    @Override
    public CompletableFuture<Order> updateOrder(Order order) {
        return CompletableFuture.supplyAsync(() -> {
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
        }, httpTaskExecutor);
    }

    @Override
    @Async("httpTaskExecutor")
    public CompletableFuture<Void> deleteOrder(Long id) {
        return CompletableFuture.runAsync(() -> {
            orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
            orderRepository.deleteById(id);
        });
    }
}
