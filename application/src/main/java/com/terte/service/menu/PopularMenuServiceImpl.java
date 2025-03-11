package com.terte.service.menu;

import com.terte.entity.menu.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PopularMenuServiceImpl implements PopularMenuService {
    private static final String MENU_COUNT_KEY_PREFIX = "store:menu_counts:"; // 매장별 주문 횟수 Hash 키
    private static final String POPULAR_MENU_KEY_PREFIX = "store:popular:";  // 매장별 인기 메뉴 Sorted Set 키
    private static final int MAX_POPULAR_MENUS = 10;  // 인기 메뉴 최대 개수

    private final StringRedisTemplate redisTemplate;
    private final MenuService menuService;

    /**
     * 특정 매장의 주문된 메뉴의 주문 횟수를 증가시킴
     */
    public void incrementMenuCount(Long storeId, Long menuId) {
        String hashKey = MENU_COUNT_KEY_PREFIX + storeId; // Hash 키: store:menu_counts:{storeId}
        String zSetKey = POPULAR_MENU_KEY_PREFIX + storeId; // Sorted Set 키: store:popular:{storeId}

        Long orderedCount = redisTemplate.opsForHash().increment(hashKey, menuId.toString(), 1);
        redisTemplate.opsForZSet().add(zSetKey, menuId.toString(), orderedCount);

        Long zSetSize = redisTemplate.opsForZSet().size(zSetKey);

        if(zSetSize!=null && zSetSize > MAX_POPULAR_MENUS) {
            //TODO: 가장 주문이 들어온지이 오래된 메뉴를 제거하도록 변경, 확률적으로 오랫동안 주문이 없던 메뉴를 제거하도록 한다.
            redisTemplate.opsForZSet().removeRange(zSetKey, 0, zSetSize - MAX_POPULAR_MENUS - 1);
        }

        System.out.println("Redis 업데이트: 매장 " + storeId + " - 메뉴 " + menuId + " 주문 횟수 증가");
    }

    /**
     * 특정 매장의 인기 메뉴 조회 (상위 N개 반환)
     */
    public List<Menu> getPopularMenus(Long storeId, int topN) {
        if(topN > MAX_POPULAR_MENUS) {
            throw new IllegalArgumentException("인기 메뉴 조회 개수는 " + MAX_POPULAR_MENUS + "개를 초과할 수 없습니다.");
        }
        if(topN <= 0) {
            throw new IllegalArgumentException("인기 메뉴 조회 개수는 0개 이하일 수 없습니다.");
        }
        String zSetKey = POPULAR_MENU_KEY_PREFIX + storeId;
        Set<String> topMenus = redisTemplate.opsForZSet().reverseRange(zSetKey, 0, topN - 1);

        List<Menu> menus = new ArrayList<>();
        if (topMenus != null) {
            for (String menuId : topMenus) {
                menus.add(menuService.getMenuById(Long.parseLong(menuId)));
            }
        }
        return menus;
    }
}
