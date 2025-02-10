package com.terte.service.order;

import com.terte.common.enums.OrderStatus;
import com.terte.entity.order.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface OrderService {
    CompletableFuture<List<Order>> getAllOrders(Long storeId, OrderStatus status);
    CompletableFuture<Order> getOrderById(Long id);
    CompletableFuture<Order> createOrder(Order order);
    CompletableFuture<Order> updateOrder(Order order);
    CompletableFuture<Void> deleteOrder(Long id);

}
