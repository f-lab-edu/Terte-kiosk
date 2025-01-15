package com.terte.controller.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.TerteMainApplication;
import com.terte.dto.menu.MenuCreateReqDTO;
import com.terte.dto.menu.MenuDetailResDTO;
import com.terte.dto.menu.MenuUpdateReqDTO;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = TerteMainApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class MenuControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setup() throws IOException {
        String sqlScript = new String(Files.readAllBytes(Paths.get("src/test/resources/sql/test-data.sql")));

        String[] sqlStatements = sqlScript.split(";");

        for (String sql : sqlStatements) {
            sql = sql.trim();
            if (!sql.isEmpty()) {
                jdbcTemplate.execute(sql);
            }
        }
    }


    @Test
    @DisplayName("카테고리 없이 전체 메뉴를 조회하면 모든 메뉴가 반환된다")
    void testGetAllMenusWithoutCategory() throws Exception {
        mockMvc.perform(get("/menus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("아메리카노"))
                .andExpect(jsonPath("$.data[0].price").value(5000));
    }

    @Test
    @DisplayName("특정 카테고리를 지정하여 요청하면 해당 카테고리의 메뉴만 반환된다")
    void testGetAllMenusWithCategory() throws Exception {

        String response = mockMvc.perform(get("/menus")
                        .param("categoryId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("아메리카노"))
                .andExpect(jsonPath("$.data[0].categoryName").value("COFFEE"))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


    @Test
    @DisplayName("존재하는 메뉴 ID로 요청 시 메뉴 상세 정보를 반환한다")
    void testGetMenuByIdSuccess() throws Exception {

        mockMvc.perform(get("/menus/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("아메리카노"))
                .andExpect(jsonPath("$.data.categoryName").value("COFFEE"))
                .andExpect(jsonPath("$.data.price").value(5000));
    }

    @Test
    @DisplayName("메뉴 상세 조회 시 존재하지 않는 메뉴 ID로 요청 시 404 Not Found를 반환한다")
    void testGetMenuByIdNotFound() throws Exception {
        mockMvc.perform(get("/menus/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메뉴 상세 조회 시 정해진 필드 외의 값은 반환되지 않는다")
    void testGetMenuByIdWithSpecificFields() throws Exception {
        String response = mockMvc.perform(get("/menus/1")
                        .param("fields", "id,name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String,Object>>() {};
        Map<String, Object> responseMap = objectMapper.readValue(response, typeReference);
        Object data = responseMap.get("data");

        assert data instanceof Map;
        Map<String, Object> dataMap = (Map<String, Object>) data;

        List<String> responseDateKeys = dataMap.keySet().stream().toList();

        List<String> MenuDetailResDTOFieldNames = Arrays.stream(MenuDetailResDTO.class.getDeclaredFields())
                .map(java.lang.reflect.Field::getName)
                .toList();

        responseDateKeys.forEach(key -> {
            assert MenuDetailResDTOFieldNames.contains(key);
        });
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
        MenuCreateReqDTO menuCreateReqDTO = new MenuCreateReqDTO("New Menu", "New Menu Description", 1000, 1L, "image.jpg");
        mockMvc.perform(post("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuCreateReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    @DisplayName("메뉴 생성 시 요청이 필수값이 누락된 경우 400 에러를 반환한다")
    void testCreateMenuMissingRequiredField() throws Exception {
        MenuCreateReqDTO menuCreateReqDTO = new MenuCreateReqDTO();

        mockMvc.perform(post("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuCreateReqDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("메뉴 수정 시 성공하면 200 OK와 수정된 메뉴 ID를 반환한다")
    void testUpdateMenuSuccess() throws Exception {

        Long targetId = 2L;
        MenuUpdateReqDTO updateMenuReqDTO = new MenuUpdateReqDTO(targetId, "Updated Menu", "Updated Menu Description", 2000, 1L, "updated-image.jpg");

        mockMvc.perform(patch("/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMenuReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(targetId));
    }


    @Test
    @DisplayName("메뉴 삭제 시 성공하면 200 OK와 삭제된 메뉴 ID를 반환한다")
    void testDeleteMenuSuccess() throws Exception {
        Long targetId = 3L;
        String deleteReqUrl = "/menus/" + targetId;
        mockMvc.perform(delete(deleteReqUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(targetId));
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