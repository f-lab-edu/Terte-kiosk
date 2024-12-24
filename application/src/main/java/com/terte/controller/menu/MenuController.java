package com.terte.controller.menu;

import com.terte.common.enums.MenuCategory;
import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.menu.*;
import com.terte.entity.category.Category;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.Option;
import com.terte.service.category.CategoryService;
import com.terte.service.menu.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
        Long storeId = 1L;
        List<Menu> menus = menuService.getAllMenus(storeId, categoryId);
        List<MenuResDTO> menuResDTOs = menus.stream().map(Menu::toMenuResDTO).toList();
        return ResponseEntity.ok(ApiResDTO.success(menuResDTOs));
    }

    /**
     * GET /menus/{menuId}
     * 특정 메뉴 상세 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResDTO<MenuDetailResDTO>> getMenuById(@PathVariable Long menuId) {
        Menu menu = menuService.getMenuById(menuId);
        MenuDetailResDTO detailMenu = menu.toMenuDetailResDTO();
        return ResponseEntity.ok(ApiResDTO.success(detailMenu));
    }

    /**
     * POST /menus
     * 메뉴 생성
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createMenu(@RequestBody @Valid CreateMenuReqDTO createMenuReqDTO) {
        Category category = null;
        if (createMenuReqDTO.getCategoryId() != null) {
            category = categoryService.getCategoryById(createMenuReqDTO.getCategoryId());
        }

        List<Option> options = (createMenuReqDTO.getOptions() != null)
                ? createMenuReqDTO.getOptions().stream()
                .map(optionDTO -> {
                    List<Choice> choices = (optionDTO.getChoices() != null)
                            ? optionDTO.getChoices().stream()
                            .map(choiceDTO -> new Choice(null, choiceDTO.getName(), choiceDTO.getPrice()))
                            .collect(Collectors.toList())
                            : Collections.emptyList();
                    return new Option(null, optionDTO.getName(), optionDTO.isMultipleSelection(), optionDTO.isRequired(), choices);
                }).collect(Collectors.toList())
                : Collections.emptyList();

        Menu menu = new Menu(
                null,
                createMenuReqDTO.getName(),
                createMenuReqDTO.getPrice(),
                category,
                1L,
                createMenuReqDTO.getImage(),
                createMenuReqDTO.getDescription(),
                options
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
            @RequestBody @Valid UpdateMenuReqDTO updateMenuReqDTO) {
        Category category = null;
        if (updateMenuReqDTO.getCategoryId() != null) {
            category = categoryService.getCategoryById(updateMenuReqDTO.getCategoryId());
        }
        List<Option> options = (updateMenuReqDTO.getOptions() != null)
                ? updateMenuReqDTO.getOptions().stream()
                .map(optionDTO -> {
                    List<Choice> choices = (optionDTO.getChoices() != null)
                            ? optionDTO.getChoices().stream()
                            .map(choiceDTO -> new Choice(null, choiceDTO.getName(), choiceDTO.getPrice()))
                            .collect(Collectors.toList())
                            : Collections.emptyList();
                    return new Option(null, optionDTO.getName(), optionDTO.isMultipleSelection(), optionDTO.isRequired(), choices);
                }).collect(Collectors.toList())
                : Collections.emptyList();
        Menu menu = new Menu(
                updateMenuReqDTO.getId(),
                updateMenuReqDTO.getName(),
                updateMenuReqDTO.getPrice(),
                category,
                1L,
                updateMenuReqDTO.getImage(),
                updateMenuReqDTO.getDescription(),
                options
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
}
