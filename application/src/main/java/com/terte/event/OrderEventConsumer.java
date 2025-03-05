package com.terte.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.dto.order.OrderEventDTO;
import com.terte.service.menu.PopularMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class OrderEventConsumer {

    private final PopularMenuService popularMenuService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-created", groupId = "popular-menu-group")
    public void consume(String message) {
        try {
            // JSON 메시지를 OrderEventDTO 객체로 변환
            OrderEventDTO orderEvent = objectMapper.readValue(message, OrderEventDTO.class);

            // Redis에 매장 ID 포함하여 인기 메뉴 반영
            orderEvent.getOrderItemDTOList().forEach(orderItemDTO -> {
                popularMenuService.incrementMenuCount(orderEvent.getStoreId(), orderItemDTO.getMenuId());
            });
            System.out.println("Kafka Consumer: 주문 이벤트 수신 - " + orderEvent);
        } catch (IOException e) {
            System.err.println("Kafka Consumer: 메시지 처리 중 오류 발생 - " + e.getMessage());
        }
    }
}

