package com.terte.service.menu;


import com.terte.entity.menu.Menu;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PopularMenuService {

    public void incrementMenuCount(Long storeId, Long menuId);
    public List<Menu> getPopularMenus(Long storeId, int topN);

}
