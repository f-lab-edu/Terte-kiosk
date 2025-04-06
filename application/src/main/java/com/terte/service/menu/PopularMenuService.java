package com.terte.service.menu;


import com.terte.entity.menu.Menu;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PopularMenuService {

    public void incrementMenuCount(Long storeId ,Map<Long, Integer> menuCountMap);
    public List<Menu> getPopularMenus(Long storeId, int topN);

}
