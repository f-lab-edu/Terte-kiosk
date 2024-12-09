package com.terte.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.common.enums.MenuCategory;
import com.terte.dto.menu.MenuResDTO;
import com.terte.service.menu.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @Test
    @DisplayName("카테고리 없이 전체 메뉴를 조회하면 모든 메뉴가 반환된다")
    void testGetAllMenusWithoutCategory() throws Exception {
        List<MenuResDTO> menus = List.of(
            new MenuResDTO(1L, "Espresso", 5000, MenuCategory.COFFEE, null),
            new MenuResDTO(1L, "팥빙수", 5000, MenuCategory.ICE_FLAKE, null)
        );

        Mockito.when(menuService.getAllMenus(null)).thenReturn(menus);

        mockMvc.perform(get("/menus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("Espresso"))
                .andExpect(jsonPath("$.data[1].name").value("팥빙수"));
    }

    @Test
    @DisplayName("특정 카테고리를 지정하여 요청하면 해당 카테고리의 메뉴만 반환된다")
    void testGetAllMenusWithCategory() throws Exception {
        List<MenuResDTO> menus = List.of(
                new MenuResDTO(1L, "Espresso", 5000, MenuCategory.COFFEE, null)
        );

        Mockito.when(menuService.getAllMenus(MenuCategory.COFFEE)).thenReturn(menus);

        mockMvc.perform(get("/menus")
                        .param("category", "COFFEE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].category").value(MenuCategory.COFFEE.name()));
    }

    @Test
    @DisplayName("결과가 비어있는 경우 빈 배열이 반환된다")
    void testGetAllMenusEmptyResult() throws Exception {
        Mockito.when(menuService.getAllMenus(null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/menus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("메뉴 조회 중 예외가 발생하면 500 에러와 에러 메시지가 반환된다")
    void testGetAllMenusServiceException() throws Exception {
        Mockito.when(menuService.getAllMenus(null)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/menus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("메뉴 조회 시 잘못된 파라미터가 전달되면 400 에러가 발생한다")
    void testGetAllMenusInvalidParameter() throws Exception {
        mockMvc.perform(get("/menus")
                        .param("category", "NOT_EXIST_CATEGORY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}