package com.terte.service.menu;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PopularMenuService {

    public void incrementMenuCount(Long storeId, Long menuId);
    public List<Long> getPopularMenus(Long storeId, int topN);

}
