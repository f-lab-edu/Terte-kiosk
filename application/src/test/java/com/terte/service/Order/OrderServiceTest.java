package com.terte.service.Order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import com.terte.common.exception.NotFoundException;
import com.terte.entity.category.Category;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.MenuOption;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import com.terte.event.OrderEventProducer;
import com.terte.repository.order.OrderRepository;
import com.terte.service.menu.MenuService;
import com.terte.service.order.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    MenuService menuService;
    @Mock
    OrderEventProducer orderEventProducer;

    private final Executor directExecutor = Runnable::run;


    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, menuService, directExecutor, orderEventProducer);
    }



    @Test
    @DisplayName("모든 주문 조회")
    void getAllOrders() {
        Long storeId = 1L;
        List<Order> orderList = List.of(new Order(1L, storeId, OrderStatus.ORDERED,10000L,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1, null), new Order(2L, storeId, OrderStatus.ORDERED,10000L,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1, null));
        when(orderRepository.findByStoreId(storeId)).thenReturn(orderList);

        CompletableFuture<List<Order>> futureResult = orderService.getAllOrders(storeId, null);
        List<Order> result = futureResult.join();

        assertEquals(2, result.size());
        verify(orderRepository,times(1)).findByStoreId(storeId);
    }

    @Test
    @DisplayName("주문 ID로 주문 조회")
    void getOrderById() {
        Long id = 1L;
        Order order = new Order(id, 1L, OrderStatus.ORDERED,12000L,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1, null);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        CompletableFuture<Order> futureResult = orderService.getOrderById(id);
        Order result = futureResult.join();

        assertEquals(id, result.getId());
        verify(orderRepository,times(1)).findById(id);
    }

    @Test
    @DisplayName("존재하지 않는 주문 조회")
    void getOrderNotFound() {
        Long id = 1L;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        // When
        CompletableFuture<Order> futureResult = orderService.getOrderById(id);

        // Then
        ExecutionException exception = assertThrows(ExecutionException.class, futureResult::get);
        assertTrue(exception.getCause() instanceof NotFoundException);
        verify(orderRepository,times(1)).findById(id);
    }

    @Test
    @DisplayName("주문 생성")
    void createOrder() {
        // Given
        SelectedOption selectedOption = new SelectedOption();
        selectedOption.setId(null);
        selectedOption.setMenuOptionId(1L);
        selectedOption.setSelectedChoiceIds(List.of(1L));

        OrderItem orderItem = new OrderItem();
        orderItem.setId(null);
        orderItem.setMenuId(1L);
        orderItem.setQuantity(2);
        orderItem.addSelectedOption(selectedOption);

        Order order = new Order();
        order.setId(null);
        order.setStoreId(1L);
        order.setStatus(OrderStatus.ORDERED);
        order.setOrderType(OrderType.DELIVERY);
        order.setPhoneNumber("010-1234-5678");
        order.setTableNumber(1);
        order.addOrderItem(orderItem);

        when(orderRepository.save(order)).thenReturn(order);
        when(menuService.getMenuByids(List.of(orderItem.getMenuId()))).thenReturn(List.of(new Menu(1L,"menu",1000,new Category(),1L,"image","desc",List.of(new MenuOption(1L,"option",true,false,null,null)))));

        // When

        CompletableFuture<Order> futureResult = orderService.createOrder(order);
        Order result = futureResult.join();

        // Then
        assertNotNull(result);
        assertEquals(order, result);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("주문 수정")
    void updateOrder() {
        Order order = new Order(1L, 1L, OrderStatus.ORDERED,100L,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1, null);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        CompletableFuture<Order> futureResult = orderService.updateOrder(order);
        Order result = futureResult.join();

        assertEquals(order, result);
        verify(orderRepository,times(1)).findById(order.getId());
        verify(orderRepository,times(1)).save(order);

    }

    @Test
    @DisplayName("존재하지 않는 주문 수정")
    void updateOrderNotFound() {
        Order order = new Order(1L, 1L, OrderStatus.ORDERED, 1000L,List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1, null);
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());


        CompletableFuture<Order> futureResult = orderService.updateOrder(order);


        ExecutionException exception = assertThrows(ExecutionException.class, futureResult::get);
        assertTrue(exception.getCause() instanceof NotFoundException);
        verify(orderRepository,times(1)).findById(order.getId());
        verify(orderRepository,never()).save(order);
    }

    @Test
    @DisplayName("주문 삭제")
    void deleteOrder() {
        Long id = 1L;
        Order order = new Order(id, 1L, OrderStatus.ORDERED, 1000L, List.of(new OrderItem()), OrderType.DELIVERY, "010-1234-5678",  1, null);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        CompletableFuture<Void> futureResult = orderService.deleteOrder(id);
        futureResult.join();

        verify(orderRepository,times(1)).findById(id);
        verify(orderRepository,times(1)).deleteById(id);
    }

    @Test
    @DisplayName("존재하지 않는 주문 삭제")
    void deleteOrderNotFound() {
        Long id = 1L;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        CompletableFuture<Void> futureResult = orderService.deleteOrder(id);

        ExecutionException exception = assertThrows(ExecutionException.class, futureResult::get);
        assertTrue(exception.getCause() instanceof NotFoundException);
        verify(orderRepository,times(1)).findById(id);
        verify(orderRepository,never()).deleteById(id);
    }
}
