package com.terte.controller.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.TerteMainApplication;
import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import com.terte.dto.menu.CategoryResDTO;
import com.terte.dto.menu.MenuResDTO;
import com.terte.dto.order.CreateOrderReqDTO;
import com.terte.dto.order.OrderDetailResDTO;
import com.terte.dto.order.UpdateOrderReqDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TerteMainApplication.class)
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("주문 리스트 조회 시 모든 주문이 반환된다")
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].status").value("ORDERED"));
    }

    @Test
    @DisplayName("주문 상세 조회 시 주문 정보가 반환된다")
    void testGetOrderById() throws Exception {
        mockMvc.perform(get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId").value(1L))
                .andExpect(jsonPath("$.data.tableNumber").exists())
                .andExpect(jsonPath("$.data.phoneNumber").exists())
                .andExpect(jsonPath("$.data.status").value("ORDERED"));
    }

    @Test
    @DisplayName("주문 상세 조회 시 정해진 필드 외의 값은 반환되지 않는다")
    void testGetOrderDetailByIdWithSpecificFields() throws Exception {
        String response = mockMvc.perform(get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String,Object>>() {};
        Map<String, Object> responseMap = objectMapper.readValue(response, typeReference);
        Object data = responseMap.get("data");

        assert data instanceof Map;
        Map<String, Object> dataMap = (Map<String,Object>) data;

        List<String> responseDateKeys = dataMap.keySet().stream().collect(Collectors.toList());

        List<String> orderDetailResDTOFieldNames = Arrays.stream(OrderDetailResDTO.class.getDeclaredFields())
                .map(java.lang.reflect.Field::getName)
                .collect(Collectors.toList());

        responseDateKeys.forEach(key -> {
            assert orderDetailResDTOFieldNames.contains(key);
        });
    }

    @Test
    @DisplayName("주문 상세 조회 시 존재하지 않는 ID면 404 Not Found를 반환한다")
    void testGetOrderByIdNotFound() throws Exception {
        mockMvc.perform(get("/orders/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("주문 생성 시 성공적으로 생성되고 생성된 ID를 반환한다")
    void testCreateOrderSuccess() throws Exception {
        CreateOrderReqDTO createOrderReqDTO = new CreateOrderReqDTO(
                List.of(new MenuResDTO(1L, "Americano", 1000, 1L, "COFFEE", "image")),
                OrderType.EATIN,
                "010-1234-5678",
                1,
                10000
        );
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(2L));
    }

    @Test
    @DisplayName("주문 수정 시 성공하면 200 OK와 수정된 주문 ID를 반환한다")
    void testUpdateOrderSuccess() throws Exception {
        UpdateOrderReqDTO updateOrderReqDTO = UpdateOrderReqDTO.builder().id(1L).orderType(OrderType.DELIVERY).build();
        mockMvc.perform(patch("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOrderReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("주문 수정 시 존재하지 않는 ID면 404 Not Found를 반환한다")
    void testUpdateOrderNotFound() throws Exception {
        UpdateOrderReqDTO updateOrderReqDTO = UpdateOrderReqDTO.builder()
                .id(999L)
                .status(OrderStatus.CANCELED)
                .build();
        mockMvc.perform(patch("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOrderReqDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("주문 삭제 시 성공하면 200 OK와 삭제된 주문 ID를 반환한다")
    void testDeleteOrderSuccess() throws Exception {
        mockMvc.perform(delete("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("주문 삭제 시 존재하지 않는 ID면 404 Not Found를 반환한다")
    void testDeleteOrderNotFound() throws Exception {
        mockMvc.perform(delete("/orders/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("주문 취소 시 유효하지 않은 ID로 요청 시 400 Bad Request를 반환한다")
    void testDeleteInvalidId() throws Exception {
        mockMvc.perform(delete("/orders/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}