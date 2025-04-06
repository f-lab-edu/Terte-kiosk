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
import java.util.concurrent.CompletableFuture;


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
    public CompletableFuture<ResponseEntity<ApiResDTO<List<PaymentResDTO>>>> getPayment() {
        Long storeId = 101L;
        return paymentService.getAllPayments(storeId).thenApply(payments -> {
            List<PaymentResDTO> paymentResDTOS = payments.stream().map(PaymentResDTO::from).toList();
            return ResponseEntity.ok(ApiResDTO.success(paymentResDTOS));
        });

    }
    /**
     * POST /payments
     * 결제 생성
     * 결제생성 타입에 따라 분기 처리
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<ApiResDTO<CommonIdResDTO>>> createPayment(@RequestBody PaymentReqDTO paymentRequest) {
        Long storeId = 1L;
        if(paymentRequest.getPaymentCreateType() == PaymentCreateType.PAY_EXISTING_ORDER){
            Long orderId = paymentRequest.getOrderId();
            if(orderId == null){
                return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
            }
            return orderService.getOrderById(orderId).thenCompose(order -> {
                Payment payment = new Payment(null, storeId, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, order.getId(), order.getTotalPrice());
                return paymentService.createPayment(payment);
            }).thenApply(payment -> ResponseEntity.ok(ApiResDTO.success(new CommonIdResDTO(payment.getId()))));

        } else if (paymentRequest.getPaymentCreateType() == PaymentCreateType.ORDER_AND_PAY){
            CreateOrderReqDTO createOrderReqDTO =  paymentRequest.getOrder();
            if(createOrderReqDTO == null){
                return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
            }
            Order saveTargetOrder = orderServiceHelper.createOrder(createOrderReqDTO, storeId);
            return orderService.createOrder(saveTargetOrder).thenCompose(savedOrder -> {
                Payment payment = new Payment(null, storeId, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, savedOrder.getId(), savedOrder.getTotalPrice());
                return paymentService.createPayment(payment);
            }).thenApply(payment -> ResponseEntity.ok(ApiResDTO.success(new CommonIdResDTO(payment.getId()))));

        }
        return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
    }
    /**
     * POST /payments/cancel/{paymentId}
     * 결제 취소
     */
    @PostMapping("/cancel/{paymentId}")
    public CompletableFuture<ResponseEntity<ApiResDTO<CommonIdResDTO>>> cancelPayment(@PathVariable Long paymentId) {
        return paymentService.cancelPayment(paymentId).thenApply(payment -> {
            return ResponseEntity.ok(ApiResDTO.success(new CommonIdResDTO(payment.getId())));
        });
    }
}
