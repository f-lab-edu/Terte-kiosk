package com.terte.controller.payment;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.PaymentCreateType;
import com.terte.common.enums.PaymentMethod;
import com.terte.common.enums.PaymentStatus;
import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.order.CreateOrderReqDTO;
import com.terte.dto.order.OrderItemDTO;
import com.terte.dto.payment.PaymentReqDTO;
import com.terte.dto.payment.PaymentResDTO;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.Option;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import com.terte.entity.payment.Payment;
import com.terte.service.menu.ChoiceService;
import com.terte.service.menu.MenuService;
import com.terte.service.menu.OptionService;
import com.terte.service.order.OrderService;
import com.terte.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final MenuService menuService;
    private final OptionService optionService;
    private final ChoiceService choiceService;

    /**
     * GET /payments
     * 결제 조회
     */
    @GetMapping
    public ResponseEntity<ApiResDTO<PaymentResDTO>> getPayment() {
        Long storeId = 1L;
        Payment payment = paymentService.getPaymentById(storeId);
        PaymentResDTO paymentResDTO = PaymentResDTO.from(payment);
        return ResponseEntity.ok(ApiResDTO.success(paymentResDTO));
    }
    /**
     * POST /payments
     * 결제 생성
     * 결제생성 타입에 따라 분기 처리
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createPayment(@RequestBody PaymentReqDTO paymentRequest) {
        Long storeId = 1L;
        Payment createdPayment = null;
        if(paymentRequest.getPaymentCreateType() == PaymentCreateType.PAY_EXISTING_ORDER){
            Long orderId = paymentRequest.getOrderId();
            if(orderId == null){
                return ResponseEntity.badRequest().build();
            }
            Order order = orderService.getOrderById(orderId);
            Payment payment = new Payment(null, storeId, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, order);
            createdPayment = paymentService.createPayment(payment);
        } else if (paymentRequest.getPaymentCreateType() == PaymentCreateType.ORDER_AND_PAY){
            CreateOrderReqDTO createOrderReqDTO =  paymentRequest.getOrder();
            if(createOrderReqDTO == null){
                return ResponseEntity.badRequest().build();
            }
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

            Payment payment = new Payment(null, storeId, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, createdOrder);

            createdPayment = paymentService.createPayment(payment);
        }
        if(createdPayment == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ApiResDTO.success(new CommonIdResDTO(createdPayment.getId())));
    }
    /**
     * POST /payments/cancel/{paymentId}
     * 결제 취소
     */
    @PostMapping("/cancel/{paymentId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> cancelPayment(@PathVariable Long paymentId) {
        paymentService.cancelPayment(paymentId);
        return ResponseEntity.ok(ApiResDTO.success(new CommonIdResDTO(paymentId)));
    }
}
