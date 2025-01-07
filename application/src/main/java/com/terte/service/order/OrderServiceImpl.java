package com.terte.service.order;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.repository.order.OrderItemRepository;
import com.terte.repository.order.OrderMapRepository;
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
        return orderRepository.findByStoreId(storeId).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        List< OrderItem> orderItemList = order.getOrderItems();
        orderItemList.forEach(orderItem -> {
            selectedOptionRepository.saveAll(orderItem.getSelectedOptions());
            orderItemRepository.save(orderItem);
        });
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
