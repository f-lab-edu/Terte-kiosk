package com.terte.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.TerteMainApplication;
import com.terte.dto.menu.CreateMenuReqDTO;
import com.terte.dto.menu.UpdateMenuReqDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = TerteMainApplication.class)
@AutoConfigureMockMvc
class MenuControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("카테고리 없이 전체 메뉴를 조회하면 모든 메뉴가 반환된다")
    void testGetAllMenusWithoutCategory() throws Exception {
        mockMvc.perform(get("/menus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("Americano"))
                .andExpect(jsonPath("$.data[1].name").value("Latte"));
    }

    @Test
    @DisplayName("특정 카테고리를 지정하여 요청하면 해당 카테고리의 메뉴만 반환된다")
    void testGetAllMenusWithCategory() throws Exception {

        mockMvc.perform(get("/menus")
                        .param("categoryId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].categoryName").value("COFFEE"));
    }

    @Test
    @DisplayName("존재하는 메뉴 ID로 요청 시 메뉴 상세 정보를 반환한다")
    void testGetMenuByIdSuccess() throws Exception {

        mockMvc.perform(get("/menus/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Americano"))
                .andExpect(jsonPath("$.data.categoryName").value("COFFEE"))
                .andExpect(jsonPath("$.data.description").value("아메리카노"));
    }

    @Test
    @DisplayName("메뉴 상세 조회 시 존재하지 않는 메뉴 ID로 요청 시 404 Not Found를 반환한다")
    void testGetMenuByIdNotFound() throws Exception {
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
    @DisplayName("메뉴가 성공적으로 생성되고 성공 후, 생성된 ID를 반환한다")
    void testCreateMenuSuccess() throws Exception {
        CreateMenuReqDTO createMenuReqDTO = CreateMenuReqDTO.builder()
                .name("모과차")
                .description("모과차 설명")
                .price(5000)
                .categoryId(3L)
                .image("image.jpg")
                .build();
        mockMvc.perform(post("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMenuReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(2L));
    }

    @Test
    @DisplayName("메뉴 생성 시 요청이 필수값이 누락된 경우 400 에러를 반환한다")
    void testCreateMenuMissingRequiredField() throws Exception {
        CreateMenuReqDTO createMenuReqDTO = CreateMenuReqDTO.builder()
                .description("아메리카노 설명")
                .build();

        mockMvc.perform(post("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMenuReqDTO)))
                .andExpect(status().isBadRequest());
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

        mockMvc.perform(patch("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMenuReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(targetId));
    }


    @Test
    @DisplayName("메뉴 삭제 시 성공하면 200 OK와 삭제된 메뉴 ID를 반환한다")
    void testDeleteMenuSuccess() throws Exception {
        mockMvc.perform(delete("/menus/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("메뉴 삭제 시 존재하지 않는 메뉴 ID로 요청 시 404 Not Found를 반환한다")
    void testDeleteMenuNotFound() throws Exception {

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