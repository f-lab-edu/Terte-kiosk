package com.terte.repository.order;

import com.terte.entity.order.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderRepository {
    private final Map<Long, Order> orderStorage = new ConcurrentHashMap<>();

    OrderRepository() {

    }

    public Order findById(Long id) {
        return orderStorage.get(id);
    }

    public Order save(Order order) {
        if(order.getId() == null) {
            order.setId(orderStorage.size() + 1L);
            orderStorage.put(order.getId(), order);
        } else {
            orderStorage.put(order.getId(), order);
        }
        return order;
    }

    public void deleteById(Long id) {
        orderStorage.remove(id);
    }
}
