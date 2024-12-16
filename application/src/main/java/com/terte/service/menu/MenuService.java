package com.terte.service.menu;

import com.terte.common.enums.MenuCategory;
import com.terte.dto.menu.CreateMenuReqDTO;
import com.terte.dto.menu.MenuDetailResDTO;
import com.terte.dto.menu.MenuResDTO;
import com.terte.dto.menu.UpdateMenuReqDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MenuService {
    Long createMenu(CreateMenuReqDTO createMenuReqDTO);
    Long updateMenu(UpdateMenuReqDTO updateMenuReqDTO);
    Long deleteMenu(Long id);

    List<MenuResDTO> getAllMenus(MenuCategory category);
    MenuDetailResDTO getMenuDetailById(Long id);
}
