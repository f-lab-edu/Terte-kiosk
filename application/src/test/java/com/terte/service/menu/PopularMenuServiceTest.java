package com.terte.service.menu;

import com.terte.entity.menu.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PopularMenuServiceTest {
    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private PopularMenuServiceImpl popularMenuService;

    @Mock
    private MenuService menuService;

    private static final String HASH_KEY_PREFIX = "store:menu_counts:";
    private static final String ZSET_KEY_PREFIX = "store:popular:";
    private static final int MAX_POPULAR_MENUS = 10;



    @Test
    @DisplayName("메뉴 카운트 증가하면 Redis에 메뉴 카운트 증가")
    void incrementMenuCount() {
        // Given
        Long storeId = 1L;
        Long menuId = 2001L;
        String hashKey = HASH_KEY_PREFIX + storeId;
        String zSetKey = ZSET_KEY_PREFIX + storeId;

        Long orderedSize = 2L;

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.increment(hashKey, menuId.toString(), 1)).thenReturn(orderedSize);

        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.size(zSetKey)).thenReturn(8L);

        // When
        popularMenuService.incrementMenuCount(storeId, menuId);

        // Then
        verify(hashOperations, times(1)).increment(hashKey, menuId.toString(), 1);
        verify(zSetOperations, times(1)).add(zSetKey, menuId.toString(), orderedSize);

    }

    @Test
    @DisplayName("메뉴 카운트 증가 시, 인기 메뉴가 MAX_POPULAR_MENUS를 초과하면 가장 후순위의 메뉴 제거")
    void incrementMenuCountAndDelete() {
        // Given
        Long storeId = 1L;
        Long menuId = 2001L;
        String hashKey = HASH_KEY_PREFIX + storeId;
        String zSetKey = ZSET_KEY_PREFIX + storeId;

        Long orderedSize = 3L;
        Long zSetSize = 12L;

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.increment(hashKey, menuId.toString(), 1)).thenReturn(orderedSize);

        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.size(zSetKey)).thenReturn(zSetSize);

        // When
        popularMenuService.incrementMenuCount(storeId, menuId);

        // Then
        verify(hashOperations, times(1)).increment(hashKey, menuId.toString(), 1);
        verify(zSetOperations, times(1)).add(zSetKey, menuId.toString(), orderedSize);
        verify(zSetOperations, times(1)).removeRange(zSetKey, 0, zSetSize - MAX_POPULAR_MENUS - 1);

    }

    @Test
    void getPopularMenus() {
        // Given
        Long storeId = 100L;
        int topN = 3;
        String zSetKey = ZSET_KEY_PREFIX + storeId;

        Set<String> mockMenuIds = Set.of("2001", "2002", "2003");
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.reverseRange(zSetKey, 0, topN - 1)).thenReturn(mockMenuIds);

        // When
        Menu menu1 = new Menu(2001L, "아메리카노", 1000, null, storeId, "image", "아메리카노 설명", null);
        Menu menu2 = new Menu(2002L, "카페라떼", 1500, null, storeId, "image", "카페라떼 설명", null);
        Menu menu3 = new Menu(2003L, "카푸치노", 2000, null, storeId, "image", "카푸치노 설명", null);

        when(menuService.getMenuById(2001L)).thenReturn(menu1);
        when(menuService.getMenuById(2002L)).thenReturn(menu2);
        when(menuService.getMenuById(2003L)).thenReturn(menu3);

        List<Menu> result = popularMenuService.getPopularMenus(storeId, topN);
        // Then
        assertEquals(3, result.size());
        assertTrue(result.contains(menu1));
        assertTrue(result.contains(menu2));
        assertTrue(result.contains(menu3));

        verify(zSetOperations, times(1)).reverseRange(zSetKey, 0, topN - 1);
    }
}