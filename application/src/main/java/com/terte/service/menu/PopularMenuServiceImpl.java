package com.terte.service.menu;

import com.terte.entity.menu.Menu;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularMenuServiceImpl implements PopularMenuService {
    private static final String STORE_MENU_LIST_PREFIX = "store:menus:"; // 매장별 메뉴 리스트 Key
    private static final String MENU_ORDER_COUNT_PREFIX = "store:%d:menu:%d"; // 매장-메뉴별 주문 횟수 Key

    private final StringRedisTemplate redisTemplate;
    private final MenuService menuService;

    /**
     * 특정 매장의 주문된 메뉴의 주문 횟수를 증가시킴 (배치 처리)
     */
    public void incrementMenuCount(Long storeId, Map<Long, Integer> menuCountMap) {
        String storeMenuListKey = STORE_MENU_LIST_PREFIX + storeId; // store:menus:{storeId}

        for (Map.Entry<Long, Integer> entry : menuCountMap.entrySet()) {
            Long menuId = entry.getKey();
            Integer count = entry.getValue();

            // store:menus:{storeId} 리스트에 menuId 추가 (중복되지 않도록)
            if (Boolean.FALSE.equals(redisTemplate.opsForList().range(storeMenuListKey, 0, -1).contains(menuId.toString()))) {
                redisTemplate.opsForList().rightPush(storeMenuListKey, menuId.toString());
            }

            // store:{storeId}:menu:{menuId} 주문 횟수 증가
            String menuOrderCountKey = String.format(MENU_ORDER_COUNT_PREFIX, storeId, menuId);
            redisTemplate.opsForValue().increment(menuOrderCountKey, count);

        }
    }

    /**
     * 특정 매장의 인기 메뉴 조회 (상위 N개 반환)
     */
    public List<Menu> getPopularMenus(Long storeId, int topN) {
        if (topN <= 0) {
            throw new IllegalArgumentException("인기 메뉴 조회 개수는 1개 이상이어야 합니다.");
        }

        String storeMenuListKey = STORE_MENU_LIST_PREFIX + storeId;
        List<String> menuIds = redisTemplate.opsForList().range(storeMenuListKey, 0, -1);
        if (menuIds == null || menuIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<MenuOrderCount> menuOrderCountList = menuIds.stream()
                .map(menuId -> {
                    String menuOrderCountKey = String.format(MENU_ORDER_COUNT_PREFIX, storeId, Long.parseLong(menuId));
                    String orderCountStr = redisTemplate.opsForValue().get(menuOrderCountKey);
                    int orderCount = (orderCountStr != null) ? Integer.parseInt(orderCountStr) : 0;
                    return new MenuOrderCount(Long.parseLong(menuId), orderCount);
                }).sorted(Comparator.comparingInt(MenuOrderCount::getOrderCount).reversed())
                .limit(topN)
                .toList();

        List<Menu> popularMenus = menuOrderCountList.stream()
                .map(MenuOrderCount::toMenu)
                .collect(Collectors.toList());

        return popularMenus;
    }

    @Getter
    @RequiredArgsConstructor
    class MenuOrderCount {
        private final Long menuId;
        private final int orderCount;

        public Menu toMenu(){
            return menuService.getMenuById(menuId);
        }
    }
}
