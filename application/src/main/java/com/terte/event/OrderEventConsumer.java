package com.terte.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.dto.order.OrderEventDTO;
import com.terte.dto.order.OrderItemDTO;
import com.terte.service.menu.PopularMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
@Service
public class OrderEventConsumer {

    private final PopularMenuService popularMenuService;
    private final ObjectMapper objectMapper;

    private final BlockingQueue<OrderEventDTO> queue = new LinkedBlockingQueue<>();
    private Acknowledgment acknowledgment; // 수동 커밋을 위해 저장

    @KafkaListener(topics = "order-created", groupId = "popular-menu-group")
    public void consume(List<String> messages, Acknowledgment acknowledgment) {
        this.acknowledgment = acknowledgment;
        for(String message : messages) {
            try {
                // JSON 메시지를 OrderEventDTO 객체로 변환
                OrderEventDTO orderEvent = objectMapper.readValue(message, OrderEventDTO.class);
                queue.add(orderEvent);
                System.out.println("Kafka Consumer: 주문 이벤트 수신 - " + orderEvent);
            } catch (IOException e) {
                System.err.println("Kafka Consumer: 메시지 처리 중 오류 발생 - " + e.getMessage());
            }
        }
    }

    @Scheduled(fixedRate = 5000) //5초마다 실행
    public void processBatch() {
        if(queue.isEmpty()) {
            return;
        }

        Map<Long, Map<Long, Integer>> storeMenuCountMap = new HashMap<>();
        System.out.println("Batch Processing Started. 메시지 수: " + queue.size());

        while(!queue.isEmpty()) {
            OrderEventDTO orderEvent = queue.poll();
            if(orderEvent != null) {
                Long storeId = orderEvent.getStoreId();
                storeMenuCountMap.putIfAbsent(storeId, new HashMap<>());

                for (OrderItemDTO orderItemDTO : orderEvent.getOrderItemDTOList()) {
                    Long menuId = orderItemDTO.getMenuId();
                    storeMenuCountMap.get(storeId).put(menuId, storeMenuCountMap.get(storeId).getOrDefault(menuId, 0) + 1);
                }
            }

            // 인기 메뉴 카운트 업데이트
            storeMenuCountMap.forEach(popularMenuService::incrementMenuCount);

            //수동커밋
            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }
        }
    }
}

