package com.terte.controller.payment;

import com.terte.common.enums.PaymentCreateType;
import com.terte.common.enums.PaymentMethod;
import com.terte.common.enums.PaymentStatus;
import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.menu.MenuResDTO;
import com.terte.dto.payment.OrderAndPayReqDTO;
import com.terte.dto.payment.PayExistingOrderReqDTO;
import com.terte.dto.payment.PaymentReqDTO;
import com.terte.dto.payment.PaymentResDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GET /payments
 * 결제 조회
 */
@RestController
@RequestMapping("/payments")
public class PaymentController {
    @GetMapping
    public ResponseEntity<ApiResDTO<PaymentResDTO>> getPayment() {
        PaymentResDTO payment = PaymentResDTO.builder()
                .id(1L)
                .paymentMethod(PaymentMethod.CASH)
                .orderId(1L)
                .status(PaymentStatus.PAYMENT_COMPLETED)
                .build();
        return ResponseEntity.ok(ApiResDTO.success(payment));
    }
    /**
     * POST /payments
     * 결제 생성
     * 결제생성 타입에 따라 분기 처리
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createPayment(@RequestBody PaymentReqDTO paymentRequest) {
        // 요청 타입에 따라 분기 처리
        if (paymentRequest instanceof OrderAndPayReqDTO) {
            OrderAndPayReqDTO orderAndPayRequest = (OrderAndPayReqDTO) paymentRequest;
        } else if (paymentRequest instanceof PayExistingOrderReqDTO) {
            PayExistingOrderReqDTO payExistingOrderRequest = (PayExistingOrderReqDTO) paymentRequest;
        } else {
            throw new IllegalArgumentException("지원하지 않는 결제 요청 타입입니다.");
        }
        return ResponseEntity.ok(ApiResDTO.success(new CommonIdResDTO(1L)));
    }
    /**
     * POST /payments/cancel/{paymentId}
     * 결제 취소
     */
    @PostMapping("/cancel/{paymentId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> cancelPayment(@PathVariable Long paymentId) {
        Long canceledPaymentId = paymentId;
        if(paymentId != 1L) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResDTO.success(new CommonIdResDTO(canceledPaymentId)));
    }
}
