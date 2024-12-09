package com.terte.controller.menu;

import com.terte.dto.common.BaseResDTO;
import com.terte.dto.common.UpdateAndDeleteResDTO;
import com.terte.dto.menu.CreateMenuReqDTO;
import com.terte.dto.menu.MenuDetailResDTO;
import com.terte.dto.menu.MenuResDTO;
import com.terte.dto.menu.UpdateMenuReqDTO;
import com.terte.service.menu.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus")
@AllArgsConstructor
public class MenuController {
    private final MenuService menuService;


    /**
     * GET /menus
     * 전체 메뉴 조회
     */
    @GetMapping
    public ResponseEntity<List<MenuResDTO>> getAllMenus(@RequestParam(required = false) String category) {
        List<MenuResDTO> menus = menuService.getAllMenus(category);
        return ResponseEntity.ok(menus);
    }

    /**
     * GET /menus/{menuId}
     * 특정 메뉴 상세 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailResDTO> getMenuById(@PathVariable Long menuId) {
        MenuDetailResDTO detailMenu = menuService.getMenuDetailById(menuId);
        return ResponseEntity.ok(detailMenu);
    }

    /**
     * POST /menus
     * 메뉴 생성
     */
    @PostMapping
    public ResponseEntity<BaseResDTO> createMenu(@RequestBody CreateMenuReqDTO createMenuReqDTO) {
        BaseResDTO createdMenuRes = menuService.createMenu(createMenuReqDTO);
        return ResponseEntity.ok(createdMenuRes);
    }

    /**
     * PATCH /menus/{menuId}
     * 메뉴 수정
     */
    @PatchMapping("/{menuId}")
    public ResponseEntity<UpdateAndDeleteResDTO> updateMenu(
            @PathVariable Long menuId,
            @RequestBody UpdateMenuReqDTO updateMenuReqDTO) {
        updateMenuReqDTO.setId(menuId); // ID 설정
        UpdateAndDeleteResDTO updatedMenuRes = menuService.updateMenu(updateMenuReqDTO);
        return ResponseEntity.ok(updatedMenuRes);
    }

    /**
     * DELETE /menus/{menuId}
     * 메뉴 삭제
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<UpdateAndDeleteResDTO> deleteMenu(@PathVariable Long menuId) {
        UpdateAndDeleteResDTO deletedMenuRes = menuService.deleteMenu(menuId);
        return ResponseEntity.ok(deletedMenuRes);
    }
}
