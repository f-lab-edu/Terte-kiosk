package com.terte.service.order;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.repository.order.OrderItemRepository;
import com.terte.repository.order.OrderRepository;
import com.terte.repository.order.SelectedOptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    //private final OrderMapRepository orderMapRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final SelectedOptionRepository selectedOptionRepository;
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
