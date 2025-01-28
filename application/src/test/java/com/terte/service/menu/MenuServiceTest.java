package com.terte.service.menu;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.category.Category;
import com.terte.entity.menu.Menu;
import com.terte.repository.menu.*;
import com.terte.service.category.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;
    @Mock
    CategoryService categoryService;
    @Mock
    OptionRepository optionRepository;
    @Mock
    ChoiceRepository choiceRepository;

    @InjectMocks
    MenuServiceImpl menuService;

    @Test
    @DisplayName("카테고리 ID없이 모든 메뉴 조회")
    void getAllMenus() {
        Long storeId = 1L;
        List<Menu> menuList = List.of(new Menu(1L, "아메리카노", 10000, new Category(), storeId,"image", "아메리카노 설명", null), new Menu(2L, "카페라떼", 15000, new Category(), storeId, "image", "카페라떼 설명", null));
        when(menuRepository.findByStoreId(storeId)).thenReturn(menuList);

        List<Menu> result = menuService.getAllMenus(storeId, null);

        assertEquals(2, result.size());
        verify(menuRepository).findByStoreId(storeId);

    }

    @Test
    @DisplayName("카테고리ID로 메뉴리스트 조회")
    void getAllMenusByCategoryId() {
        Long storeId = 1L;
        Long categoryId = 1L;
        Category category = new Category(categoryId, "음료", storeId,"설명");
        List<Menu> menuList = List.of(new Menu(1L, "아메리카노", 10000, category, storeId,"image", "아메리카노 설명", null), new Menu(2L, "카페라떼", 15000, category, storeId, "image", "카페라떼 설명", null));
        when(menuRepository.findByCategoryId(category.getId())).thenReturn(menuList);

        List<Menu> result = menuService.getAllMenus(storeId, categoryId);
        assertEquals(2, result.size());
        verify(menuRepository).findByCategoryId(category.getId());


    }

    @Test
    @DisplayName("메뉴 ID로 메뉴 조회")
    void getMenuById() {
        Long menuId = 1L;
        Menu menu = new Menu(menuId, "아메리카노", 10000, new Category(), 1L, "image", "아메리카노 설명", null);
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        Menu result = menuService.getMenuById(menuId);

        assertEquals(menuId, result.getId());
        verify(menuRepository).findById(menuId);
    }

    @Test
    @DisplayName("메뉴 ID로 존재하지 않는 메뉴 조회")
    void getMenuByIdNotFound() {
        Long menuId = 1L;
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> menuService.getMenuById(menuId));
        verify(menuRepository,times(1)).findById(menuId);
    }

    @Test
    @DisplayName("메뉴 생성")
    void createMenu() {
        Long storeId = 1L;
        Long categoryId = 1L;
        Category category = new Category(categoryId, "음료", storeId, "설명");
        Menu menu = new Menu(null, "아메리카노", 10000, category, storeId, "image", "아메리카노 설명", null);
        when(menuRepository.save(menu)).thenReturn(menu);

        Menu result = menuService.createMenu(menu);

        assertNotNull(result);
        assertEquals("아메리카노", result.getName());
        verify(menuRepository,times(1)).save(menu);
    }


    @Test
    @DisplayName("존재하는 메뉴 수정")
    void updateMenu() {
        Long storeId = 1L;
        Menu existingMenu = new Menu(1L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of());
        Menu updatedMenu = new Menu(1L, "아메리카노", 15000, null, storeId, "image", "아메리카노 설명", List.of());

        when(menuRepository.findById(1L)).thenReturn(Optional.of(existingMenu));
        when(menuRepository.save(updatedMenu)).thenReturn(updatedMenu);


        Menu result = menuService.updateMenu(updatedMenu);

        assertEquals("아메리카노", result.getName());
        assertEquals("아메리카노 설명", result.getDescription());
        assertEquals(15000, result.getPrice());
        verify(menuRepository).findById(1L);
        verify(menuRepository).save(updatedMenu);
        verify(optionRepository, never()).save(any());
        verify(choiceRepository, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 수정")
    void updateMenuNotFound() {
        Long storeId = 1L;
        Menu notExistingMenu = new Menu(2L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of());

        when(menuRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> menuService.updateMenu(notExistingMenu));
        verify(menuRepository).findById(2L);
        verify(menuRepository, never()).save(any());
    }

    @Test
    @DisplayName("일부 필드가 누락된 채로 메뉴 수정")
    void updateMenuWithMissingFields() {
        Long storeId = 1L;
        Menu existingMenu = new Menu(3L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of());
        Menu partialUpdateMenu = new Menu(3L, null, 777, null, storeId, null, null,null);
        Menu expectedUpdatedMenu = new Menu(3L, "아메리카노", 777, null, storeId, "image", "아메리카노 설명", List.of());

        when(menuRepository.findById(3L)).thenReturn(Optional.of(existingMenu));
        when(menuRepository.save(any())).thenReturn(expectedUpdatedMenu);

        Menu result = menuService.updateMenu(partialUpdateMenu);

        assertEquals("아메리카노", result.getName());
        assertEquals("아메리카노 설명", result.getDescription());
        assertEquals(777, result.getPrice());
        verify(menuRepository).findById(3L);
        verify(menuRepository).save(any());
    }


    @Test
    @DisplayName("메뉴 삭제")
    void deleteMenu() {
        Long menuId = 3L;

        Menu existingMenu = new Menu(menuId, "아메리카노", 10000, null, 1L, "image", "아메리카노 설명", List.of());

        when(menuRepository.findById(menuId)).thenReturn(Optional.of(existingMenu));

        menuService.deleteMenu(menuId);

        verify(menuRepository,times(1)).deleteById(menuId);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 삭제")
    void deleteMenuNotFound() {
        Long menuId = 3L;

        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> menuService.deleteMenu(menuId));

        verify(menuRepository, never()).deleteById(menuId);
        verify(optionRepository, never()).deleteById(any());
        verify(choiceRepository, never()).deleteById(any());
    }

}