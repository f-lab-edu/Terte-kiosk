package com.terte.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.common.enums.MenuCategory;
import com.terte.dto.menu.CreateMenuReqDTO;
import com.terte.dto.menu.MenuDetailResDTO;
import com.terte.dto.menu.MenuResDTO;
import com.terte.dto.menu.UpdateMenuReqDTO;
import com.terte.service.menu.MenuService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @BeforeEach
    void setUp() {
        Mockito.reset(menuService);
    }

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

    @Test
    @DisplayName("존재하는 메뉴 ID로 요청 시 메뉴 상세 정보를 반환한다")
    void testGetMenuByIdSuccess() throws Exception {
        MenuDetailResDTO menuDetail = new MenuDetailResDTO(1L, "Americano","아메리카노 설명", 5000, MenuCategory.COFFEE, null, null);

        Mockito.when(menuService.getMenuDetailById(1L)).thenReturn(menuDetail);

        mockMvc.perform(get("/menus/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Americano"))
                .andExpect(jsonPath("$.data.category").value("COFFEE"))
                .andExpect(jsonPath("$.data.description").value("아메리카노 설명"));
    }

    @Test
    @DisplayName("메뉴 상세 조회 시 존재하지 않는 메뉴 ID로 요청 시 404 Not Found를 반환한다")
    void testGetMenuByIdNotFound() throws Exception {
        Mockito.when(menuService.getMenuDetailById(999L))
                .thenReturn(null);
        //TODO: 예외를 던져서 API 응답을 처리하는 방법으로 변경 - 컨트롤러에서 예외를 던지는 것이 아니라, 서비스에서 예외를 던지도록 변경
        mockMvc.perform(get("/menus/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메뉴 상세 조회 시 유효하지 않은 ID로 요청 시 400 Bad Request를 반환한다")
    void testGetMenuByIdInvalidId() throws Exception {
        mockMvc.perform(get("/menus/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 상세 조회 시 서비스에서 예외 발생 시 500 Internal Server Error를 반환한다")
    void testGetMenuByIdServiceException() throws Exception {
        Mockito.when(menuService.getMenuDetailById(1L))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/menus/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("메뉴가 성공적으로 생성되고 성공 후, 생성된 ID를 반환한다")
    void testCreateMenuSuccess() throws Exception {
        CreateMenuReqDTO createMenuReqDTO = CreateMenuReqDTO.builder()
                .name("아메리카노")
                .description("아메리카노 설명")
                .price(5000)
                .category(MenuCategory.COFFEE)
                .image("americano.jpg")
                .build();

        Mockito.when(menuService.createMenu(any(CreateMenuReqDTO.class))).thenReturn(1L);

        mockMvc.perform(post("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMenuReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("메뉴 생성 시 요청이 필수값이 누락된 경우 400 에러를 반환한다")
    void testCreateMenuMissingRequiredField() throws Exception {
        CreateMenuReqDTO createMenuReqDTO = CreateMenuReqDTO.builder()
                .name("아메리카노")
                .description("아메리카노 설명")
                .build();

        mockMvc.perform(post("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMenuReqDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 생성 시 서비스에서 예외 발생 시 500 Internal Server Error를 반환한다")
    void testCreateMenuServiceException() throws Exception {
        CreateMenuReqDTO createMenuReqDTO = CreateMenuReqDTO.builder()
                .name("아메리카노")
                .description("아메리카노 설명")
                .price(5000)
                .category(MenuCategory.COFFEE)
                .image("americano.jpg")
                .build();

        Mockito.when(menuService.createMenu(any(CreateMenuReqDTO.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMenuReqDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("메뉴 수정 시 성공하면 200 OK와 수정된 메뉴 ID를 반환한다")
    void testUpdateMenuSuccess() throws Exception {
        Long targetId = 1L;
        UpdateMenuReqDTO updateMenuReqDTO = UpdateMenuReqDTO.builder()
                .id(targetId)
                .price(9000)
                .image("image url")
                .build();
        Mockito.when(menuService.updateMenu(any(UpdateMenuReqDTO.class)))
                .thenReturn(updateMenuReqDTO.getId());
        mockMvc.perform(patch("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMenuReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(targetId));
    }

    @Test
    @DisplayName("메뉴 수정 시 서비스에서 예외 발생 시 500 Internal Server Error를 반환한다")
    void testUpdateMenuServiceException() throws Exception {
        Mockito.when(menuService.updateMenu(any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(patch("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UpdateMenuReqDTO.builder().id(1L).build())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("메뉴 삭제 시 성공하면 200 OK와 삭제된 메뉴 ID를 반환한다")
    void testDeleteMenuSuccess() throws Exception {
        Mockito.when(menuService.deleteMenu(1L))
                .thenReturn(1L);

        mockMvc.perform(delete("/menus/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("메뉴 삭제 시 존재하지 않는 메뉴 ID로 요청 시 404 Not Found를 반환한다")
    void testDeleteMenuNotFound() throws Exception {
        Mockito.when(menuService.deleteMenu(999L))
                .thenReturn(null);

        mockMvc.perform(delete("/menus/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메뉴 삭제 시 유효하지 않은 ID로 요청 시 400 Bad Request를 반환한다")
    void testDeleteMenuInvalidId() throws Exception {
        mockMvc.perform(delete("/menus/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}