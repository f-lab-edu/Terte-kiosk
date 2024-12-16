package com.terte.controller.menu;

import com.terte.common.enums.MenuCategory;
import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.menu.*;
import com.terte.service.menu.MenuService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus")
@AllArgsConstructor
public class MenuController {


    /**
     * GET /menus
     * 전체 메뉴 조회
     */
    @GetMapping
    public ResponseEntity<ApiResDTO<List<MenuResDTO>>> getAllMenus(@RequestParam(required = false) Long categoryId) {
        MenuResDTO menu1 = MenuResDTO.builder()
                .id(1L)
                .name("Americano")
                .price(5000)
                .categoryId(1L)
                .categoryName("COFFEE")
                .image("https://image.com")
                .build();
        MenuResDTO menu2 = MenuResDTO.builder()
                .id(2L)
                .name("Latte")
                .price(6000)
                .categoryId(1L)
                .categoryName("COFFEE")
                .image("https://image.com")
                .build();
        List<MenuResDTO> menus = List.of(menu1, menu2);
        if(categoryId != null) {
            menus = menus.stream()
                    .filter(menu -> menu.getCategoryId().equals(categoryId))
                    .toList();
        }
        return ResponseEntity.ok(ApiResDTO.success(menus));
    }

    /**
     * GET /menus/{menuId}
     * 특정 메뉴 상세 조회
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResDTO<MenuDetailResDTO>> getMenuById(@PathVariable Long menuId) {
        MenuDetailResDTO detailMenu = MenuDetailResDTO.builder()
                .id(1L)
                .name("Americano")
                .description("아메리카노")
                .price(5000)
                .categoryId(1L)
                .categoryName("COFFEE")
                .image("https://image.com")
                .options(List.of())
                .build();
        if (menuId != 1L) {
            //TODO: 예외를 던져서 API 응답을 처리하는 방법으로 변경 - 컨트롤러에서 예외를 던지는 것이 아니라, 서비스에서 예외를 던지도록 변경
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResDTO.success(detailMenu));
    }

    /**
     * POST /menus
     * 메뉴 생성
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createMenu(@RequestBody @Valid CreateMenuReqDTO createMenuReqDTO) {
        createMenuReqDTO.toString();
        Long createdMenuId = 2L;
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(createdMenuId).build()));
    }

    /**
     * PATCH /menus
     * 메뉴 수정
     */
    @PatchMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateMenu(
            @RequestBody @Valid UpdateMenuReqDTO updateMenuReqDTO) {
        Long updatedMenuId = updateMenuReqDTO.getId();
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(updatedMenuId).build()));
    }

    /**
     * DELETE /menus/{menuId}
     * 메뉴 삭제
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> deleteMenu(@PathVariable Long menuId) {
        Long deletedId = menuId;
        if(deletedId != 1L) {
            //TODO: 예외를 던져서 API 응답을 처리하는 방법으로 변경 - 컨트롤러에서 예외를 던지는 것이 아니라, 서비스에서 예외를 던지도록 변경
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(deletedId).build()));
    }
}
