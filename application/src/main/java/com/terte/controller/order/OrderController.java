package com.terte.controller.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.order.*;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.Option;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import com.terte.service.menu.ChoiceService;
import com.terte.service.menu.MenuService;
import com.terte.service.menu.OptionService;
import com.terte.service.order.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MenuService menuService;
    private final OptionService optionService;
    private final ChoiceService choiceService;
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
        Long storeId = 1L;
        List<OrderItemDTO> orderItemDTOList = createOrderReqDTO.getOrderItemList();
        List<OrderItem> orderItemList = orderItemDTOList.stream().map(orderItemDTO -> {
            Menu menu = menuService.getMenuById(orderItemDTO.getMenuId());
            List<SelectedOption> selectedOptionList =  orderItemDTO.getSelectedOptions().stream().map(selectedOptionDTO -> {
                Option option = optionService.getOptionById(selectedOptionDTO.getOptionId());
                List<Choice> choices = selectedOptionDTO.getSelectedChoiceIds().stream().map(choiceService::getChoiceById).collect(Collectors.toList());
                return new SelectedOption(null, option, choices);
            }).collect(Collectors.toList());
            return new OrderItem(null, menu, orderItemDTO.getQuantity(), selectedOptionList);
        }).toList();

        Order order = new Order(null, storeId, OrderStatus.ORDERED, orderItemList,createOrderReqDTO.getOrderType(),createOrderReqDTO.getPhoneNumber(),createOrderReqDTO.getTableNumber());
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
        Long storeId = 1L;
        Order order = new Order(updateOrderReqDTO.getId(), storeId, updateOrderReqDTO.getStatus(), null, updateOrderReqDTO.getOrderType(), updateOrderReqDTO.getPhoneNumber(), updateOrderReqDTO.getTableNumber());
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
