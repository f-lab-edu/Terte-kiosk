package com.terte.service.menu;

import com.terte.common.enums.MenuCategory;
import com.terte.dto.menu.CreateMenuReqDTO;
import com.terte.dto.menu.MenuDetailResDTO;
import com.terte.dto.menu.MenuResDTO;
import com.terte.dto.menu.UpdateMenuReqDTO;
import com.terte.entity.menu.Menu;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MenuService {
    List<Menu> getAllMenus(Long storeId, Long CategoryId);
    Menu getMenuById(Long id);
    Menu createMenu(Menu menu);
    Menu updateMenu(Menu menu);
    void deleteMenu(Long id);
}
