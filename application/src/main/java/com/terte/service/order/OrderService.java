package com.terte.service.order;

import com.terte.common.enums.OrderStatus;
import com.terte.entity.order.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    List<Order> getAllOrders(Long storeId, OrderStatus status);
    Order getOrderById(Long id);
    Order createOrder(Order order);
    Order updateOrder(Order order);
    void deleteOrder(Long id);

}
