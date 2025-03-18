package com.terte.service.menu;

import com.terte.entity.menu.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PopularMenuServiceTest {
    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ListOperations<String, String> listOperations;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private MenuService menuService;

    @InjectMocks
    private PopularMenuServiceImpl popularMenuService;

    private static final String STORE_MENU_LIST_PREFIX = "store:menus:";
    private static final String MENU_ORDER_COUNT_PREFIX = "store:%d:menu:%d";

    @Test
    @DisplayName("메뉴 카운트 증가 시, Redis에 메뉴 ID 추가 및 주문 횟수 증가")
    void incrementMenuCount() {
        // Given
        Long storeId = 1L;
        Map<Long, Integer> menuCountMap = Map.of(2001L, 2, 2002L, 3);

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // When
        popularMenuService.incrementMenuCount(storeId, menuCountMap);

        // Then
        for (Map.Entry<Long, Integer> entry : menuCountMap.entrySet()) {
            Long menuId = entry.getKey();
            Integer count = entry.getValue();

            String storeMenuListKey = STORE_MENU_LIST_PREFIX + storeId;
            String menuOrderCountKey = String.format(MENU_ORDER_COUNT_PREFIX, storeId, menuId);

            verify(listOperations, times(1)).rightPush(storeMenuListKey, menuId.toString());
            verify(valueOperations, times(1)).increment(menuOrderCountKey, count);
        }
    }

    @Test
    @DisplayName("메뉴 카운트 증가 후 중복 저장되지 않도록 검증")
    void incrementMenuCount_NoDuplicateEntries() {
        // Given
        Long storeId = 1L;
        Map<Long, Integer> menuCountMap = Map.of(2001L, 2);

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(listOperations.range(STORE_MENU_LIST_PREFIX + storeId, 0, -1))
                .thenReturn(List.of("2001")); // 이미 존재하는 메뉴

        // When
        popularMenuService.incrementMenuCount(storeId, menuCountMap);

        // Then
        verify(listOperations, never()).rightPush(STORE_MENU_LIST_PREFIX + storeId, "2001");
        verify(valueOperations, times(1)).increment(String.format(MENU_ORDER_COUNT_PREFIX, storeId, 2001L), 2);
    }

    @Test
    @DisplayName("인기 메뉴 조회 - 주문 횟수 기준 정렬 후 상위 N개 반환")
    void getPopularMenus() {
        // Given
        Long storeId = 100L;
        int topN = 3;
        String storeMenuListKey = STORE_MENU_LIST_PREFIX + storeId;

        // Redis에서 메뉴 리스트 가져오기
        List<String> storedMenuIds = Arrays.asList("2001", "2002", "2003", "2004");
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range(storeMenuListKey, 0, -1)).thenReturn(storedMenuIds);

        // Redis에서 각 메뉴의 주문 횟수 가져오기
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(String.format(MENU_ORDER_COUNT_PREFIX, storeId, 2001L))).thenReturn("5");
        when(valueOperations.get(String.format(MENU_ORDER_COUNT_PREFIX, storeId, 2002L))).thenReturn("8");
        when(valueOperations.get(String.format(MENU_ORDER_COUNT_PREFIX, storeId, 2003L))).thenReturn("3");
        when(valueOperations.get(String.format(MENU_ORDER_COUNT_PREFIX, storeId, 2004L))).thenReturn("7");

        // 실제 Menu 객체를 반환하도록 설정
        Menu menu1 = new Menu(2001L, "아메리카노", 1000, null, storeId, "image", "아메리카노 설명", null);
        Menu menu2 = new Menu(2002L, "카페라떼", 1500, null, storeId, "image", "카페라떼 설명", null);
        Menu menu3 = new Menu(2004L, "카푸치노", 2000, null, storeId, "image", "카푸치노 설명", null);

        when(menuService.getMenuById(2001L)).thenReturn(menu1);
        when(menuService.getMenuById(2002L)).thenReturn(menu2);
        when(menuService.getMenuById(2004L)).thenReturn(menu3);

        // When
        List<Menu> result = popularMenuService.getPopularMenus(storeId, topN);

        // Then
        assertEquals(3, result.size());
        assertEquals(menu2, result.get(0)); // 가장 주문 수가 많은 메뉴 (8)
        assertEquals(menu3, result.get(1)); // 두 번째 주문 수 (7)
        assertEquals(menu1, result.get(2)); // 세 번째 주문 수 (5)

        verify(listOperations, times(1)).range(storeMenuListKey, 0, -1);
        verify(valueOperations, times(storedMenuIds.size())).get(anyString());
    }
}
