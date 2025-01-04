package com.terte.service.Order;

import com.terte.common.enums.OrderType;
import com.terte.common.enums.OrderStatus;
import com.terte.common.exception.NotFoundException;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.repository.order.OrderRepository;
import com.terte.service.order.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    @DisplayName("모든 주문 조회")
    void getAllOrders() {
        Long storeId = 1L;
        List<Order> orderList = List.of(new Order(1L, storeId, OrderStatus.ORDERED,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1), new Order(2L, storeId, OrderStatus.ORDERED,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1));
        when(orderRepository.findByStoreId(storeId)).thenReturn(orderList);

        List<Order> result = orderService.getAllOrders(storeId);

        assertEquals(2, result.size());
        verify(orderRepository,times(1)).findByStoreId(storeId);
    }

    @Test
    @DisplayName("주문 ID로 주문 조회")
    void getOrderById() {
        Long id = 1L;
        Order order = new Order(id, 1L, OrderStatus.ORDERED,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1);
        when(orderRepository.findById(id)).thenReturn(order);

        Order result = orderService.getOrderById(id);

        assertEquals(id, result.getId());
        verify(orderRepository,times(1)).findById(id);
    }

    @Test
    @DisplayName("존재하지 않는 주문 조회")
    void getOrderNotFound() {
        Long id = 1L;
        when(orderRepository.findById(id)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> orderService.getOrderById(id));
        verify(orderRepository,times(1)).findById(id);
    }

    @Test
    @DisplayName("주문 생성")
    void createOrder() {
        Order order = new Order(1L, 1L, OrderStatus.ORDERED,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertEquals(order, result);
        verify(orderRepository,times(1)).save(order);
    }

    @Test
    @DisplayName("주문 수정")
    void updateOrder() {
        Order order = new Order(1L, 1L, OrderStatus.ORDERED,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1);
        when(orderRepository.findById(order.getId())).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.updateOrder(order);

        assertEquals(order, result);
        verify(orderRepository,times(1)).findById(order.getId());
        verify(orderRepository,times(1)).save(order);

    }

    @Test
    @DisplayName("존재하지 않는 주문 수정")
    void updateOrderNotFound() {
        Order order = new Order(1L, 1L, OrderStatus.ORDERED,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1);
        when(orderRepository.findById(order.getId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> orderService.updateOrder(order));
        verify(orderRepository,times(1)).findById(order.getId());
        verify(orderRepository,never()).save(order);
    }

    @Test
    @DisplayName("주문 삭제")
    void deleteOrder() {
        Long id = 1L;
        Order order = new Order(id, 1L, OrderStatus.ORDERED,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1);
        when(orderRepository.findById(id)).thenReturn(order);

        orderService.deleteOrder(id);

        verify(orderRepository,times(1)).findById(id);
        verify(orderRepository,times(1)).deleteById(id);
    }

    @Test
    @DisplayName("존재하지 않는 주문 삭제")
    void deleteOrderNotFound() {
        Long id = 1L;
        when(orderRepository.findById(id)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> orderService.deleteOrder(id));
        verify(orderRepository,times(1)).findById(id);
        verify(orderRepository,never()).deleteById(id);
    }
}