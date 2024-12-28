package com.terte.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.TerteMainApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TerteMainApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChoiceControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("옵션ID로 선택지를 조회할 때 존재하지 않는 옵션ID를 요청하면 404를 반환한다.")
    void testGetChoicesByNonExistingOptionId() {
    }

    @Test
    @DisplayName("선택지를 생성한다.")
    void testCreateChoice() {
    }

    @Test
    @DisplayName("선택지를 수정한다.")
    void testUpdateChoice() {
    }

    @Test
    @DisplayName("존재하지 않는 선택지를 수정하려고 하면 404를 반환한다.")
    void testUpdateChoiceWithNonExistingChoice() {
    }

    @Test
    @DisplayName("선택지를 삭제한다.")
    void testDeleteChoice() {
    }

    @Test
    @DisplayName("존재하지 않는 선택지를 삭제하려고 하면 404를 반환한다.")
    void testDeleteNonExistingChoice() {
    }

}
