package com.terte.service.menu;

import com.terte.entity.menu.Menu;
import com.terte.entity.menu.MenuOption;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface MenuService {
    List<Menu> getAllMenus(Long storeId, Long CategoryId);
    Menu getMenuById(Long id);
    Menu createMenu(Menu menu);
    Menu updateMenu(Menu menu);
    void deleteMenu(Long id);
    Set<MenuOption> getOptionsById(Long id);

    List<Menu> getMenuByids(List<Long> ids);

    List<Menu> getMenuWithOptionsAndChoicesByIds(List<Long> ids);

}
