package com.terte.controller.menu;

import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.menu.*;
import com.terte.entity.category.Category;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.MenuOption;
import com.terte.service.category.CategoryService;
import com.terte.service.menu.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final CategoryService categoryService;


    /**
     * GET /menus
     * 전체 메뉴 조회
     */
    @GetMapping
    public ResponseEntity<ApiResDTO<List<MenuResDTO>>> getAllMenus(@RequestParam(required = false) Long categoryId) {
        Long storeId = 101L;
        List<Menu> menus = menuService.getAllMenus(storeId, categoryId);
        List<MenuResDTO> menuResDTOs = menus.stream().map(MenuResDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResDTO.success(menuResDTOs));
    }

    /**
     * GET /menus/{menuId}
     * 특정 메뉴 상세 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResDTO<MenuDetailResDTO>> getMenuById(@PathVariable Long menuId) {
        Menu menu = menuService.getMenuById(menuId);
        MenuDetailResDTO detailMenu = MenuDetailResDTO.from(menu);
        return ResponseEntity.ok(ApiResDTO.success(detailMenu));
    }

    /**
     * POST /menus
     * 메뉴 생성
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createMenu(@RequestBody @Valid MenuCreateReqDTO menuCreateReqDTO) {
        Category category = null;
        if (menuCreateReqDTO.getCategoryId() != null) {
            category = categoryService.getCategoryById(menuCreateReqDTO.getCategoryId());
        }

        Menu menu = new Menu(
                null,
                menuCreateReqDTO.getName(),
                menuCreateReqDTO.getPrice(),
                category,
                101L,
                menuCreateReqDTO.getImage(),
                menuCreateReqDTO.getDescription(),
                null
        );

        Menu createdMenu = menuService.createMenu(menu);
        Long createdMenuId = createdMenu.getId();

        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(createdMenuId).build()));
    }

    /**
     * PATCH /menus
     * 메뉴 수정
     */
    @PatchMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateMenu(
            @RequestBody @Valid MenuUpdateReqDTO updateMenuReqDTO) {
        Category category = null;
        if (updateMenuReqDTO.getCategoryId() != null) {
            category = categoryService.getCategoryById(updateMenuReqDTO.getCategoryId());
        }
        List<MenuOption> menuOptions =  menuService.getOptionsById(updateMenuReqDTO.getId());
        Menu menu = new Menu(
                updateMenuReqDTO.getId(),
                updateMenuReqDTO.getName(),
                updateMenuReqDTO.getPrice(),
                category,
                101L,
                updateMenuReqDTO.getImage(),
                updateMenuReqDTO.getDescription(),
                menuOptions
        );
        Long updatedMenuId = menuService.updateMenu(menu).getId();
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(updatedMenuId).build()));
    }

    /**
     * DELETE /menus/{menuId}
     * 메뉴 삭제
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(menuId).build()));
    }

    /**
     * GET /options/{menuId}
     * 특정 메뉴의 옵션 조회
     */
    @GetMapping("/{menuId}/options")
    public ResponseEntity<ApiResDTO<List<OptionResDTO>>> getOptionsById(@PathVariable Long menuId) {
        List<MenuOption> menuOptions = menuService.getOptionsById(menuId);
        List<OptionResDTO> optionResDTOS = menuOptions.stream().map(OptionResDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResDTO.success(optionResDTOS));
    }
}


