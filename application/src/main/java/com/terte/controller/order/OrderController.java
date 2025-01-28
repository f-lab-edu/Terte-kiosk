package com.terte.controller.order;

import com.terte.common.enums.OrderStatus;
import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.order.*;
import com.terte.entity.order.Order;
import com.terte.service.helper.OrderServiceHelper;
import com.terte.service.order.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderServiceHelper orderServiceHelper;
    /**
     *
     * Get /orders
     * 주문 리스트 조회
     */
    @GetMapping
    public ResponseEntity<ApiResDTO<List<OrderResDTO>>> getAllOrders(@RequestParam(required = false) String status) {
        Long storeId = 101L;
        OrderStatus orderStatus = null;
        if(status != null){
            orderStatus = OrderStatus.valueOf(status);
        }
        List<Order> orders = orderService.getAllOrders(storeId,orderStatus);
        List<OrderResDTO> orderResDTOS = orders.stream().map(OrderResDTO::from).toList();

        return ResponseEntity.ok(ApiResDTO.success(orderResDTOS));
    }

    /**
     * Post /categories/{orderId}
     * 주문 상세 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResDTO<OrderDetailResDTO>> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        OrderDetailResDTO orderDetail = OrderDetailResDTO.from(order);

        return ResponseEntity.ok(ApiResDTO.success(orderDetail));

    }

    /**
     * Post /categories
     * 주문 생성
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createOrder(@RequestBody CreateOrderReqDTO createOrderReqDTO) {
        Long storeId = 101L;
        Order order = orderServiceHelper.createOrder(createOrderReqDTO, storeId);
        Order createdOrder = orderService.createOrder(order);

        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(createdOrder.getId()).build()));
    }
    /**
     * PATCH /categories
     * 주문 수정
     * 주문 상태, 주문 타입, 전화번호, 테이블 번호 수정
     * 주문 메뉴 리스트의 수정은 불가능(삭제 후 재성성 해야한다.)
     */
    @PatchMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateOrder(@RequestBody UpdateOrderReqDTO updateOrderReqDTO) {
        Long storeId = 101L;
        Order order = new Order(updateOrderReqDTO.getId(), storeId, updateOrderReqDTO.getStatus(), null ,null, updateOrderReqDTO.getOrderType(), updateOrderReqDTO.getPhoneNumber(), updateOrderReqDTO.getTableNumber(),null);
        Order updatedOrder = orderService.updateOrder(order);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(updatedOrder.getId()).build()));
    }
    /**
     * DELETE /categories
     * 주문 삭제
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(orderId).build()));
    }
}
