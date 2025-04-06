package com.terte.controller.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.TerteMainApplication;
import com.terte.common.enums.OrderType;
import com.terte.common.enums.PaymentCreateType;
import com.terte.common.enums.PaymentMethod;
import com.terte.common.enums.PaymentStatus;
import com.terte.dto.order.CreateOrderReqDTO;
import com.terte.dto.order.OrderItemDTO;
import com.terte.dto.order.SelectedOptionDTO;
import com.terte.dto.payment.PaymentReqDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TerteMainApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class PaymentControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("결제 조회 시 결제 정보가 반환된다")
    void testGetPayment() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/payments")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].paymentMethod").value(PaymentMethod.CREDIT_CARD.name()))
                .andExpect(jsonPath("$.data[0].orderId").value(2L))
                .andExpect(jsonPath("$.data[0].status").value(PaymentStatus.PAYMENT_COMPLETED.name()));
    }

    @Test
    @DisplayName("결제 생성 시 성공하면 200 OK와 생성된 결제 ID를 반환한다 - 주문 및 결제")
    void testCreatePaymentOrderAndPaySuccess() throws Exception {
        SelectedOptionDTO selectedOptionDTO = new SelectedOptionDTO(1L,List.of(1L));
        OrderItemDTO orderItemDTO =  new OrderItemDTO(1L,1,List.of(selectedOptionDTO));
        CreateOrderReqDTO createOrderReqDTO = new CreateOrderReqDTO(
                List.of(orderItemDTO),
                OrderType.EATIN,
                "010-1234-5678",
                1,
                10000
        );
        PaymentReqDTO orderAndPayReqDTO = PaymentReqDTO.builder()
                .paymentCreateType(PaymentCreateType.ORDER_AND_PAY)
                .order(createOrderReqDTO)
                .paymentMethod(PaymentMethod.CASH)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderAndPayReqDTO))).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    @DisplayName("결제 생성 시 성공하면 200 OK와 생성된 결제 ID를 반환한다 - 기존 주문 결제")
    void testCreatePaymentPayExistingOrderSuccess() throws Exception {
        PaymentReqDTO paymentReqDTO = PaymentReqDTO.builder()
                .paymentCreateType(PaymentCreateType.PAY_EXISTING_ORDER)
                .orderId(1L)
                .paymentMethod(PaymentMethod.CASH)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentReqDTO))).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }
    @Test
    @DisplayName("결제 취소 시 성공하면 200 OK와 취소된 결제 ID를 반환한다")
    void testCancelPaymentSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/payments/cancel/1")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("결제 취소 시 존재하지 않는 결제 ID로 요청 시 404 Not Found를 반환한다")
    void testCancelPaymentNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/payments/cancel/999")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("결제 취소 시 유효하지 않은 ID로 요청 시 400 Bad Request를 반환한다")
    void testCancelPaymentInvalidId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/payments/cancel/invalid-id")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());
    }

}