package com.terte.controller.payment;

import com.terte.common.enums.PaymentCreateType;
import com.terte.common.enums.PaymentMethod;
import com.terte.common.enums.PaymentStatus;
import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.order.CreateOrderReqDTO;
import com.terte.dto.payment.PaymentReqDTO;
import com.terte.dto.payment.PaymentResDTO;
import com.terte.entity.order.Order;
import com.terte.entity.payment.Payment;
import com.terte.service.helper.OrderServiceHelper;
import com.terte.service.order.OrderService;
import com.terte.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final OrderServiceHelper orderServiceHelper;

    /**
     * GET /payments
     * 결제 조회
     */
    @GetMapping
    public ResponseEntity<ApiResDTO<List<PaymentResDTO>>> getPayment() {
        Long storeId = 101L;
        List<Payment> payments = paymentService.getAllPayments(storeId);
        List<PaymentResDTO> paymentResDTOS = payments.stream().map(PaymentResDTO::from).toList();
        return ResponseEntity.ok(ApiResDTO.success(paymentResDTOS));
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
            Payment payment = new Payment(null, storeId, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, order.getId(), order.getTotalPrice());
            createdPayment = paymentService.createPayment(payment);
        } else if (paymentRequest.getPaymentCreateType() == PaymentCreateType.ORDER_AND_PAY){
            CreateOrderReqDTO createOrderReqDTO =  paymentRequest.getOrder();
            if(createOrderReqDTO == null){
                return ResponseEntity.badRequest().build();
            }
            Order saveTargetOrder = orderServiceHelper.createOrder(createOrderReqDTO, storeId);
            Order savedOrder = orderService.createOrder(saveTargetOrder);
            Payment payment = new Payment(null, storeId, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, savedOrder.getId(), savedOrder.getTotalPrice());

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
