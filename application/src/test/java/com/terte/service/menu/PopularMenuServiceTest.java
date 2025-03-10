package com.terte.service.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PopularMenuServiceTest {
    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private PopularMenuServiceImpl popularMenuService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @Test
    void incrementMenuCount_ShouldIncreaseScoreInRedis() {
        // Given
        Long storeId = 1L;
        Long menuId = 2001L;
        String redisKey = "popular-menus:" + storeId;

        // When
        popularMenuService.incrementMenuCount(storeId, menuId);

        // Then
        verify(zSetOperations, times(1)).incrementScore(redisKey, String.valueOf(menuId), 1.0);
    }

    @Test
    void getPopularMenus_ShouldReturnTopNMenus() {
        // Given
        Long storeId = 100L;
        int topN = 3;
        String redisKey = "popular-menus:" + storeId;

        Set<String> mockMenuIds = Set.of("2001", "2002", "2003");
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.reverseRange(redisKey, 0, topN - 1)).thenReturn(mockMenuIds);

        // When
        var result = popularMenuService.getPopularMenus(storeId, topN);

        // Then
        assertEquals(3, result.size());
        assertTrue(result.contains(2001L));
        assertTrue(result.contains(2002L));
        assertTrue(result.contains(2003L));

        verify(zSetOperations, times(1)).reverseRange(redisKey, 0, topN - 1);
    }
}