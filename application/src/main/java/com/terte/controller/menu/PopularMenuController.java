package com.terte.controller.menu;

import com.terte.dto.common.ApiResDTO;
import com.terte.dto.menu.MenuResDTO;
import com.terte.entity.menu.Menu;
import com.terte.service.menu.PopularMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PopularMenuController {

    private final PopularMenuService popularMenuService;
    @GetMapping("/popular-menus")
    public ResponseEntity<ApiResDTO<List<MenuResDTO>>> getPopularMenus(@RequestParam(defaultValue = "5") int topN) {
        Long storeId = 1L;
        List<Menu> menus = popularMenuService.getPopularMenus(storeId, topN);
        List<MenuResDTO> menuResDTOs = menus.stream().map(MenuResDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResDTO.success(menuResDTOs));
    }
}
