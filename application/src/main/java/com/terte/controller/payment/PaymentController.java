package com.terte.controller.payment;

import com.terte.common.enums.PaymentMethod;
import com.terte.common.enums.PaymentStatus;
import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.payment.PaymentReqDTO;
import com.terte.dto.payment.PaymentResDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payments")
public class PaymentController {

    /**
     * GET /payments
     * 결제 조회
     */
    @GetMapping
    public ResponseEntity<ApiResDTO<PaymentResDTO>> getPayment() {
        PaymentResDTO payment = new PaymentResDTO(1L, 1L, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED);
        return ResponseEntity.ok(ApiResDTO.success(payment));
    }
    /**
     * POST /payments
     * 결제 생성
     * 결제생성 타입에 따라 분기 처리
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createPayment(@RequestBody PaymentReqDTO paymentRequest) {
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
