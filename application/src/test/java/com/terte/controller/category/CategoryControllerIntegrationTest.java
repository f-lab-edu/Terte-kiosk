package com.terte.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.TerteMainApplication;
import com.terte.dto.menu.CreateCategoryReqDTO;
import com.terte.dto.menu.UpdateCategoryReqDTO;
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
class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("카테고리 조회 시 모든 카테고리가 반환된다")
    void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("COFFEE"));
    }


    @Test
    @DisplayName("카테고리가 성공적으로 생성되고 성공 후, 생성된 ID를 반환한다")
    void testCreateCategorySuccess() throws Exception {
        CreateCategoryReqDTO createCategoryReqDTO = CreateCategoryReqDTO.builder()
                .name("New Category")
                .build();
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(3L));
    }

    @Test
    @DisplayName("카테고리 생성 시 요청이 필수값이 누락된 경우 400 에러를 반환한다")
    void testCreateCategoryMissingRequiredField() throws Exception {
        CreateCategoryReqDTO createCategoryReqDTO = CreateCategoryReqDTO.builder()
                .build();

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryReqDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("카테고리 수정 시 성공하면 200 OK와 수정된 카테고리 ID를 반환한다")
    void testUpdateCategorySuccess() throws Exception {
        Long targetId = 1L;
        UpdateCategoryReqDTO updateCategoryReqDTO = UpdateCategoryReqDTO.builder()
                .id(targetId)
                .name("Updated Category")
                .build();

        mockMvc.perform(patch("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("카테고리 수정 시 존재하지 않는 카테고리 ID로 요청 시 404 Not Found를 반환한다")
    void testUpdateCategoryNotFound() throws Exception {
        UpdateCategoryReqDTO updateCategoryReqDTO = UpdateCategoryReqDTO.builder()
                .id(999L)
                .name("Updated Category")
                .build();

        mockMvc.perform(patch("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryReqDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("카테고리 삭제 시 성공하면 200 OK와 삭제된 카테고리 ID를 반환한다")
    void testDeleteCategorySuccess() throws Exception {
        mockMvc.perform(delete("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    @DisplayName("카테고리 삭제 시 존재하지 않는 카테고리 ID로 요청 시 404 Not Found를 반환한다")
    void testDeleteCategoryNotFound() throws Exception {
        mockMvc.perform(delete("/categories/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("카테고리 삭제 시 유효하지 않은 ID로 요청 시 400 Bad Request를 반환한다")
    void testDeleteCategoryInvalidId() throws Exception {
        mockMvc.perform(delete("/categories/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }




}