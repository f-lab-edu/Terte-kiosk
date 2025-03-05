package com.terte.service.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PopularMenuServiceImpl implements PopularMenuService {
    private static final String REDIS_KEY_PREFIX = "popular-menus:";  // 매장별 인기 메뉴 키
    private final StringRedisTemplate redisTemplate;

    /**
     * 특정 매장의 주문된 메뉴의 주문 횟수를 증가시킴
     */
    public void incrementMenuCount(Long storeId, Long menuId) {
        String redisKey = REDIS_KEY_PREFIX + storeId; // 매장별 키 생성
        redisTemplate.opsForZSet().incrementScore(redisKey, String.valueOf(menuId), 1);
        System.out.println("Redis 업데이트: 매장 " + storeId + " - 메뉴 " + menuId + " 주문 횟수 증가");
    }

    /**
     * 특정 매장의 인기 메뉴 조회 (상위 N개 반환)
     */
    public List<Long> getPopularMenus(Long storeId, int topN) {
        String redisKey = REDIS_KEY_PREFIX + storeId;
        Set<String> topMenus = redisTemplate.opsForZSet().reverseRange(redisKey, 0, topN - 1);

        List<Long> menuIds = new ArrayList<>();
        if (topMenus != null) {
            for (String menuId : topMenus) {
                menuIds.add(Long.parseLong(menuId));
            }
        }
        return menuIds;
    }
}
