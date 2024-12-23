package com.terte.controller.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.order.CreateOrderReqDTO;
import com.terte.dto.order.OrderDetailResDTO;
import com.terte.dto.order.OrderResDTO;
import com.terte.dto.order.UpdateOrderReqDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    //private final OrderService orderService;
    /**
     *
     * Get /orders
     * 주문 리스트 조회
     */
    @GetMapping
    public ResponseEntity<ApiResDTO<List<OrderResDTO>>> getAllOrders(@RequestParam(required = false) String status) {
        //List<OrderResDTO> orders = orderService.getAllOrders(status);
        List<OrderResDTO> orders = List.of(
                new OrderResDTO(1L, List.of(), OrderType.EATIN, OrderStatus.ORDERED, "2024-12-13 12:00:00", 10000),
                new OrderResDTO(2L, List.of(), OrderType.TAKEOUT, OrderStatus.ORDERED, "2024-12-13 12:00:00", 10000)
        );
        if(status != null) {
            orders = orders.stream()
                    .filter(order -> order.getStatus().name().equals(status))
                    .toList();
        }
        return ResponseEntity.ok(ApiResDTO.success(orders));
    }

    /**
     * Post /categories/{orderId}
     * 주문 상세 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResDTO<OrderDetailResDTO>> getOrderById(@PathVariable Long orderId) {
        //orderDetailResDTO orderDetail = orderService.getOrderDetailById(orderId);
        if (orderId != 1L) {
            return ResponseEntity.notFound().build();
        }
        OrderDetailResDTO orderDetail = new OrderDetailResDTO(1L, List.of(), OrderType.EATIN, OrderStatus.ORDERED, "2024-12-13 12:00:00", "010-1234-5678", 1);
        return ResponseEntity.ok(ApiResDTO.success(orderDetail));

    }

    /**
     * Post /categories
     * 주문 생성
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createOrder(@RequestBody CreateOrderReqDTO createOrderReqDTO) {
        //Long createdOrderId = orderService.createOrder(createOrderReqDTO);
        Long createdOrderId = 2L;
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(createdOrderId).build()));
    }
    /**
     * PATCH /categories
     * 주문 수정
     */
    @PatchMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateOrder(@RequestBody UpdateOrderReqDTO updateOrderReqDTO) {
        //Long updatedOrderId = orderService.updateOrder(updateOrderReqDTO);
        if(updateOrderReqDTO.getId() != 1L) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(updateOrderReqDTO.getId()).build()));
    }
    /**
     * DELETE /categories
     * 주문 삭제
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> deleteOrder(@PathVariable Long orderId) {
       // Long deletedOrderId = orderService.deleteOrder(orderId);
        if(orderId != 1L) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(orderId).build()));
    }
}
