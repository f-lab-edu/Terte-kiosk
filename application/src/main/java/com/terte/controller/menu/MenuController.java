package com.terte.controller.menu;

import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
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
    public ResponseEntity<ApiResDTO<List<MenuResDTO>>> getAllMenus(@RequestParam(required = false) String category) {
        List<MenuResDTO> menus = menuService.getAllMenus(category);
        return ResponseEntity.ok(ApiResDTO.success(menus));
    }

    /**
     * GET /menus/{menuId}
     * 특정 메뉴 상세 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResDTO<MenuDetailResDTO>> getMenuById(@PathVariable Long menuId) {
        MenuDetailResDTO detailMenu = menuService.getMenuDetailById(menuId);
        return ResponseEntity.ok(ApiResDTO.success(detailMenu));
    }

    /**
     * POST /menus
     * 메뉴 생성
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createMenu(@RequestBody CreateMenuReqDTO createMenuReqDTO) {
        Long createdMenuId = menuService.createMenu(createMenuReqDTO);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(createdMenuId).build()));
    }

    /**
     * PATCH /menus/{menuId}
     * 메뉴 수정
     */
    @PatchMapping("/{menuId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateMenu(
            @PathVariable Long menuId,
            @RequestBody UpdateMenuReqDTO updateMenuReqDTO) {
        updateMenuReqDTO.setId(menuId); // ID 설정
        Long updatedMenuId = menuService.updateMenu(updateMenuReqDTO);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(updatedMenuId).build()));
    }

    /**
     * DELETE /menus/{menuId}
     * 메뉴 삭제
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> deleteMenu(@PathVariable Long menuId) {
        Long deletedId = menuService.deleteMenu(menuId);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(deletedId).build()));
    }
}
