package com.terte.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.dto.order.OrderEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public void sendOrderEvent(OrderEventDTO orderEvent) {
        try {
            String message = objectMapper.writeValueAsString(orderEvent); // DTO → JSON 변환
            kafkaTemplate.send("order-created", message);
        } catch (Exception e) {
            throw new RuntimeException("Kafka 메시지 변환 오류", e);
        }
    }
}
