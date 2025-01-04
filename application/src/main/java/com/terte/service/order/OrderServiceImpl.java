package com.terte.service.order;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.order.Order;
import com.terte.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    @Override
    public List<Order> getAllOrders(Long storeId) {
        return orderRepository.findByStoreId(storeId);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order order) {
        Order existingOrder = orderRepository.findById(order.getId());
        if (existingOrder == null) {
            throw new NotFoundException("Order not found");
        }
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

    }
}
