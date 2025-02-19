package com.terte.repository.order;

import com.terte.common.enums.OrderStatus;
import com.terte.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStoreId(Long storeId);
    List<Order> findByStoreIdAndStatus(Long storeId, OrderStatus status);
}
