package com.terte.controller.payment;

import com.terte.common.enums.OrderStatus;
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
import com.terte.service.order.OrderService;
import com.terte.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

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
        if(paymentRequest.getPaymentCreateType() == PaymentCreateType.PAY_EXISTING_ORDER){
            Long orderId = paymentRequest.getOrderId();
            if(orderId == null){
                return ResponseEntity.badRequest().build();
            }
            Payment payment = new Payment(null, storeId, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED, orderId);
            paymentService.createPayment(payment);
        } else if (paymentRequest.getPaymentCreateType() == PaymentCreateType.ORDER_AND_PAY){
            CreateOrderReqDTO createOrderReqDTO =  paymentRequest.getOrder();
            if(createOrderReqDTO == null){
                return ResponseEntity.badRequest().build();
            }
            //TODO: 주문 생성 후 결제 생성
        }
        return ResponseEntity.ok(ApiResDTO.success(new CommonIdResDTO(1L)));
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
