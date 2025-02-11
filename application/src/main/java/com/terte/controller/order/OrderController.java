package com.terte.controller.order;

import com.terte.common.enums.OrderStatus;
import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.order.*;
import com.terte.entity.order.Order;
import com.terte.service.helper.OrderServiceHelper;
import com.terte.service.order.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<ResponseEntity<ApiResDTO<List<OrderResDTO>>>> getAllOrders(@RequestParam(required = false) String status) {
        Long storeId = 101L;
        OrderStatus orderStatus = null;
        if(status != null){
            orderStatus = OrderStatus.valueOf(status);
        }
        return orderService.getAllOrders(storeId,orderStatus)
                .thenApply(
                orders -> {
                    List<OrderResDTO> orderResDTOS = orders.stream().map(OrderResDTO::from).toList();
                    return ResponseEntity.ok(ApiResDTO.success(orderResDTOS));
                }
        )
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResDTO.error("Error fetching orders: " + ex.getMessage())));
    }

    /**
     * Post /categories/{orderId}
     * 주문 상세 조회
     */
    @GetMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<ApiResDTO<OrderDetailResDTO>>> getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId).thenApply(
                order -> {
                    OrderDetailResDTO orderDetail = OrderDetailResDTO.from(order);
                    return ResponseEntity.ok(ApiResDTO.success(orderDetail));
                }
        ).exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResDTO.error("Error fetching order: " + ex.getMessage())));
    }

    /**
     * Post /categories
     * 주문 생성
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<ApiResDTO<CommonIdResDTO>>> createOrder(@RequestBody CreateOrderReqDTO createOrderReqDTO) {
        Long storeId = 101L;
        Order order = orderServiceHelper.createOrder(createOrderReqDTO, storeId);
        return orderService.createOrder(order).thenApply(
                createdOrder -> ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(createdOrder.getId()).build()))
        ).exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResDTO.error("Error creating order: " + ex.getMessage())));
    }
    /**
     * PATCH /categories
     * 주문 수정
     * 주문 상태, 주문 타입, 전화번호, 테이블 번호 수정
     * 주문 메뉴 리스트의 수정은 불가능(삭제 후 재성성 해야한다.)
     */
    @PatchMapping
    public CompletableFuture<ResponseEntity<ApiResDTO<CommonIdResDTO>>> updateOrder(@RequestBody UpdateOrderReqDTO updateOrderReqDTO) {
        Long storeId = 101L;
        Order order = new Order(updateOrderReqDTO.getId(), storeId, updateOrderReqDTO.getStatus(), null ,null, updateOrderReqDTO.getOrderType(), updateOrderReqDTO.getPhoneNumber(), updateOrderReqDTO.getTableNumber(),null);
        return orderService.updateOrder(order).thenApply(
                updatedOrder -> ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(updatedOrder.getId()).build()))
        ).exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResDTO.error("Error updating order: " + ex.getMessage())));
    }
    /**
     * DELETE /categories
     * 주문 삭제
     */
    @DeleteMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<ApiResDTO<CommonIdResDTO>>> deleteOrder(@PathVariable Long orderId) {
        return orderService.deleteOrder(orderId).thenApply(
                v -> ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(orderId).build()))
        ).exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResDTO.error("Error deleting order: " + ex.getMessage())));
    }
}
