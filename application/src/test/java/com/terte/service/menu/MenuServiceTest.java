package com.terte.service.menu;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.category.Category;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.Option;
import com.terte.repository.menu.ChoiceRepository;
import com.terte.repository.menu.MenuRepository;
import com.terte.repository.menu.OptionRepository;
import com.terte.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
        Category category = new Category(categoryId, "음료", storeId);
        List<Menu> menuList = List.of(new Menu(1L, "아메리카노", 10000, category, storeId,"image", "아메리카노 설명", null), new Menu(2L, "카페라떼", 15000, category, storeId, "image", "카페라떼 설명", null));
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);
        when(menuRepository.findByCategory(category)).thenReturn(menuList);

        List<Menu> result = menuService.getAllMenus(storeId, categoryId);
        assertEquals(2, result.size());
        verify(menuRepository).findByCategory(category);


    }

    @Test
    @DisplayName("메뉴 ID로 메뉴 조회")
    void getMenuById() {
        Long menuId = 1L;
        Menu menu = new Menu(menuId, "아메리카노", 10000, new Category(), 1L, "image", "아메리카노 설명", null);
        when(menuRepository.findById(menuId)).thenReturn(menu);

        Menu result = menuService.getMenuById(menuId);

        assertEquals(menuId, result.getId());
        verify(menuRepository).findById(menuId);
    }

    @Test
    @DisplayName("메뉴 ID로 존재하지 않는 메뉴 조회")
    void getMenuByIdNotFound() {
        Long menuId = 1L;
        when(menuRepository.findById(menuId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> menuService.getMenuById(menuId));
        verify(menuRepository).findById(menuId);
    }

    @Test
    @DisplayName("옵션과 초이스 없이 메뉴만 생성")
    void createMenu() {
        Long storeId = 1L;
        Long categoryId = 1L;
        Category category = new Category(categoryId, "음료", storeId);
        Menu menu = new Menu(null, "아메리카노", 10000, category, storeId, "image", "아메리카노 설명", null);
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);
        when(menuRepository.save(menu)).thenReturn(menu);

        Menu result = menuService.createMenu(menu);

        assertNotNull(result);
        assertEquals("아메리카노", result.getName());
        verify(menuRepository).save(menu);
    }

    @Test
    @DisplayName("옵션만 포함된 메뉴 생성")
    void createMenuWithOptions() {
        Long storeId = 1L;
        Long categoryId = 1L;
        Category category = new Category(categoryId, "음료", storeId);
        List<Option> optionList = List.of(new Option(null, "샷 추가", false, false, null));
        Menu menu = new Menu(null, "아메리카노", 10000, category, storeId, "image", "아메리카노 설명", optionList);
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);
        when(optionRepository.save(optionList.get(0))).thenReturn(optionList.get(0));
        when(menuRepository.save(menu)).thenReturn(menu);

        Menu result = menuService.createMenu(menu);

        assertNotNull(result);
        assertEquals("아메리카노", result.getName());
        verify(menuRepository).save(menu);
        verify(optionRepository).save(optionList.get(0));

    }

    @Test
    @DisplayName("옵션과 초이스가 포함된 메뉴 생성")
    void createMenuWithOptionsAndChoices() {
        Long storeId = 1L;
        Long categoryId = 1L;
        Category category = new Category(categoryId, "음료", storeId);
        List<Choice> newChoiceList = List.of(new Choice(null, "샷 추가", 500));
        List<Option> newOptionList = List.of(new Option(null, "샷 추가", false, false, newChoiceList));
        List<Choice> createdChoiceList = List.of(new Choice(1L, "샷 추가", 500));
        List<Option> createdOptionList = List.of(new Option(1L, "샷 추가", false, false, createdChoiceList));

        Menu menu = new Menu(null, "아메리카노", 10000, category, storeId, "image", "아메리카노 설명", newOptionList);
        when(categoryService.getCategoryById(categoryId)).thenReturn(category);
        when(optionRepository.save(any())).thenReturn(createdOptionList.get(0));
        when(choiceRepository.save(any())).thenReturn(createdChoiceList.get(0));
        when(menuRepository.save(menu)).thenReturn(menu);

        Menu result = menuService.createMenu(menu);

        assertNotNull(result);
        assertEquals("아메리카노", result.getName());
        assertNotNull(result.getOptions().get(0).getId());
        verify(menuRepository).save(menu);
        verify(optionRepository).save(createdOptionList.get(0));
        verify(choiceRepository).save(createdChoiceList.get(0));
    }

    @Test
    @DisplayName("존재하는 메뉴 수정")
    void updateMenu() {
        Long storeId = 1L;
        Menu existingMenu = new Menu(1L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of());
        Menu updatedMenu = new Menu(1L, "아메리카노", 15000, null, storeId, "image", "아메리카노 설명", List.of());

        when(menuRepository.findById(1L)).thenReturn(existingMenu);
        when(menuRepository.save(updatedMenu)).thenReturn(updatedMenu);


        Menu result = menuService.updateMenu(updatedMenu);

        assertEquals("아메리카노", result.getName());
        assertEquals("아메리카노 설명", result.getDescription());
        assertEquals(15000, result.getPrice());
        verify(menuRepository).findById(1L);
        verify(menuRepository).save(updatedMenu);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 수정")
    void updateMenuNotFound() {
        Long storeId = 1L;
        Menu notExistingMenu = new Menu(2L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of());

        when(menuRepository.findById(2L)).thenReturn(null);

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

        when(menuRepository.findById(3L)).thenReturn(existingMenu);
        when(menuRepository.save(any())).thenReturn(expectedUpdatedMenu);

        Menu result = menuService.updateMenu(partialUpdateMenu);

        assertEquals("아메리카노", result.getName());
        assertEquals("아메리카노 설명", result.getDescription());
        assertEquals(777, result.getPrice());
        verify(menuRepository).findById(3L);
        verify(menuRepository).save(any());
    }

    @Test
    @DisplayName("메뉴에 옵션이 추가된 경우")
    void updateMenuWithNewOptions() {
        // Arrange
        Long storeId = 1L;
        Menu existingMenu = new Menu(3L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of());
        Option newOption = new Option(null, "추가 옵션", true, false, null);

        Menu updatedMenu = new Menu(3L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of(newOption));
        Option savedOption = new Option(100L, "추가 옵션", true, false, null);

        when(menuRepository.findById(3L)).thenReturn(existingMenu);
        when(optionRepository.save(newOption)).thenReturn(savedOption);
        when(menuRepository.save(updatedMenu)).thenReturn(updatedMenu);

        // Act
        Menu result = menuService.updateMenu(updatedMenu);

        // Assert
        assertEquals(1, result.getOptions().size());
        assertEquals("추가 옵션", result.getOptions().get(0).getName());
        assertNotNull(result.getOptions().get(0).getId());
        verify(optionRepository).save(newOption);
        verify(menuRepository).save(updatedMenu);
    }

    @Test
    @DisplayName("메뉴에 옵션이 업데이트 된 경우")
    void updateMenuWithUpdatedOptions() {
        // Arrange
        Long storeId = 1L;
        Option existingOption = new Option(100L, "기존 옵션", true, false, null);
        Menu existingMenu = new Menu(3L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of(existingOption));

        Option updatedOption = new Option(100L, "업데이트된 옵션", true, false, null);
        Menu updatedMenu = new Menu(3L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of(updatedOption));

        when(menuRepository.findById(3L)).thenReturn(existingMenu);
        when(optionRepository.findById(100L)).thenReturn(existingOption);
        when(optionRepository.save(updatedOption)).thenReturn(updatedOption);
        when(menuRepository.save(updatedMenu)).thenReturn(updatedMenu);

        // Act
        Menu result = menuService.updateMenu(updatedMenu);

        // Assert
        assertEquals(1, result.getOptions().size());
        assertEquals("업데이트된 옵션", result.getOptions().get(0).getName());
        verify(optionRepository).findById(100L);
        verify(optionRepository).save(updatedOption);
        verify(menuRepository).save(updatedMenu);
    }


    @Test
    @DisplayName("메뉴에 초이스가 추가된 경우")
    void updateMenuWithNewChoices() {
        // Arrange
        Long storeId = 1L;
        Choice newChoice = new Choice(null, "샷 추가", 500);
        Choice savedChoice = new Choice(200L, "샷 추가", 500);
        Option existingOption = new Option(100L, "기존 옵션", false, false, List.of());
        Option updatedOption = new Option(100L, "기존 옵션", false, false, List.of(savedChoice));
        Option updateOption = new Option(100L, "기존 옵션", false, false, List.of(newChoice));
        Menu existingMenu = new Menu(3L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of(existingOption));
        Menu updateMenu = new Menu(3L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of(updateOption));
        Menu updatedMenu = new Menu(3L, "아메리카노", 10000, null, storeId, "image", "아메리카노 설명", List.of(updatedOption));

        when(menuRepository.findById(3L)).thenReturn(existingMenu);
        when(optionRepository.findById(100L)).thenReturn(existingOption);
        when(choiceRepository.save(newChoice)).thenReturn(savedChoice);
        when(optionRepository.save(updateOption)).thenReturn(updatedOption);
        when(menuRepository.save(any())).thenReturn(updatedMenu);

        Menu result = menuService.updateMenu(updateMenu);

        assertNotNull(result);
        assertEquals(1, result.getOptions().size());
        assertEquals(1, result.getOptions().get(0).getChoices().size());
        assertEquals("샷 추가", result.getOptions().get(0).getChoices().get(0).getName());
        assertEquals(200L, result.getOptions().get(0).getChoices().get(0).getId());

        verify(choiceRepository).save(newChoice);
        verify(optionRepository).save(existingOption);
        verify(menuRepository).save(any());
    }


    @Test
    @DisplayName("메뉴에 초이스가 업데이트 된 경우")
    void updateMenuWithUpdatedChoices() {
    }

    @Test
    @DisplayName("메뉴 삭제")
    void deleteMenu() {
        Long menuId = 3L;
        Long optionId = 100L;
        Long choiceId = 200L;

        Choice existingChoice = new Choice(choiceId, "샷 추가", 500);
        Option existingOption = new Option(optionId, "기존 옵션", false, false, List.of(existingChoice));
        Menu existingMenu = new Menu(menuId, "아메리카노", 10000, null, 1L, "image", "아메리카노 설명", List.of(existingOption));

        when(menuRepository.findById(menuId)).thenReturn(existingMenu);

        menuService.deleteMenu(menuId);

        verify(menuRepository).deleteById(menuId);
        verify(optionRepository).deleteById(optionId);
        verify(choiceRepository).deleteById(choiceId);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 삭제")
    void deleteMenuNotFound() {
        Long menuId = 3L;

        when(menuRepository.findById(menuId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> menuService.deleteMenu(menuId));

        verify(menuRepository, never()).deleteById(menuId);
        verify(optionRepository, never()).deleteById(any());
        verify(choiceRepository, never()).deleteById(any());
    }

}