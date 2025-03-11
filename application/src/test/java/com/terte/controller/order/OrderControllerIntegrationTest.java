package com.terte.controller.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.TerteMainApplication;
import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import com.terte.dto.order.*;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TerteMainApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 리스트 조회 시 모든 주문이 반환된다")
    void testGetAllOrders() throws Exception {
        MvcResult mvcResult =  mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();

        ResultActions perform = mockMvc.perform(asyncDispatch(mvcResult));
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].status").value("ORDERED"));

    }

    @Test
    @DisplayName("주문 상태를 통해 주문 리스트를 조회한다.")
    void testGetAllOrdersByStatus() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders?status=ORDERED")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].status").value("ORDERED"));
    }

    @Test
    @DisplayName("주문 상세 조회 시 주문 정보가 반환된다")
    void testGetOrderById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
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

        List<String> responseDateKeys = dataMap.keySet().stream().toList();

        List<String> orderDetailResDTOFieldNames = Arrays.stream(OrderDetailResDTO.class.getDeclaredFields())
                .map(java.lang.reflect.Field::getName)
                .toList();

        responseDateKeys.forEach(key -> {
            assert orderDetailResDTOFieldNames.contains(key);
        });
    }

    @Test
    @DisplayName("주문 상세 조회 시 존재하지 않는 ID면 404 Not Found를 반환한다")
    void testGetOrderByIdNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders/999")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("주문 생성 시 성공적으로 생성되고 생성된 ID를 반환한다")
    void testCreateOrderSuccess() throws Exception {
        SelectedOptionDTO selectedOptionDTO = new SelectedOptionDTO(1L,List.of(1L));
        OrderItemDTO orderItemDTO =  new OrderItemDTO(1L,1,List.of(selectedOptionDTO));
        CreateOrderReqDTO createOrderReqDTO = new CreateOrderReqDTO(
                List.of(orderItemDTO),
                OrderType.EATIN,
                "010-1234-5678",
                1,
                10000
        );
        MvcResult mvcResult = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderReqDTO))).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    @DisplayName("주문 생성 시 필수 선택 옵션의 값이 없으면 400 Bad Request를 반환한다")
    void testCreateOrderWithoutRequiredOption() throws Exception {
        SelectedOptionDTO selectedOptionDTO = new SelectedOptionDTO(1L,List.of(1L));
        OrderItemDTO orderItemDTO =  new OrderItemDTO(3L,1,List.of(selectedOptionDTO));
        CreateOrderReqDTO createOrderReqDTO = new CreateOrderReqDTO(
                List.of(orderItemDTO),
                OrderType.EATIN,
                "010-1234-5678",
                1,
                10000
        );
        MvcResult mvcResult = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderReqDTO))).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문 생성 시 중복 선택 불가 옵션의 다수가 선택되면 400 Bad Request를 반환한다")
    void testCreateOrderWithDuplicateOption() throws Exception {
        SelectedOptionDTO selectedOptionDTO1 = new SelectedOptionDTO(4L,List.of(5L,6L));
        OrderItemDTO orderItemDTO =  new OrderItemDTO(3L,1,List.of(selectedOptionDTO1));
        CreateOrderReqDTO createOrderReqDTO = new CreateOrderReqDTO(
                List.of(orderItemDTO),
                OrderType.EATIN,
                "010-1234-5678",
                1,
                10000
        );
        MvcResult mvcResult = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderReqDTO))).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문 수정 시 성공하면 200 OK와 수정된 주문 ID를 반환한다")
    void testUpdateOrderSuccess() throws Exception {
        UpdateOrderReqDTO updateOrderReqDTO = UpdateOrderReqDTO.builder().id(1L).orderType(OrderType.DELIVERY).build();
        MvcResult mvcResult = mockMvc.perform(patch("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateOrderReqDTO))).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
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
        MvcResult mvcResult = mockMvc.perform(patch("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateOrderReqDTO))).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("주문 삭제 시 성공하면 200 OK와 삭제된 주문 ID를 반환한다")
    void testDeleteOrderSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("주문 삭제 시 존재하지 않는 ID면 404 Not Found를 반환한다")
    void testDeleteOrderNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/orders/999")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("주문 취소 시 유효하지 않은 ID로 요청 시 400 Bad Request를 반환한다")
    void testDeleteInvalidId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/orders/invalid-id")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());
    }
}