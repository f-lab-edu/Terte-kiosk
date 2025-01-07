package com.terte.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.TerteMainApplication;
import com.terte.dto.menu.MenuCreateReqDTO;
import com.terte.dto.menu.OptionCreateReqDTO;
import com.terte.dto.menu.OptionUpdateReqDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MenuOptionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("옵션ID로 선택지를 조회한다.")
    void testGetChoicesByOptionId() throws Exception{
        Long targetOptionId = 1L;
        mockMvc.perform(get("/options/" + targetOptionId + "/choices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("샷 추가"))
                .andExpect(jsonPath("$.data[0].price").value(500));
    }

    @Test
    @DisplayName("존재하지 않는 옵션ID로 선택지를 조회하면 404를 반환한다.")
    void testGetChoicesByNonExistingOptionId() throws Exception{
        Long targetOptionId = 100L;
        mockMvc.perform(get("/options/" + targetOptionId + "/choices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("옵션을 생성한다")
    void testCreateOption() throws Exception{
        MenuCreateReqDTO menuCreateReqDTO = new MenuCreateReqDTO("New Menu", "New Menu Description", 1000, 101L, "image.jpg");
        String MenuId = mockMvc.perform(post("/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateReqDTO))).andReturn().getResponse().getContentAsString();

        OptionCreateReqDTO optionCreateReqDTO = new OptionCreateReqDTO("샷 추가", false, true, Long.parseLong(MenuId));
        mockMvc.perform(post("/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionCreateReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(3L));

    }

    @Test
    @DisplayName("옵션을 생성할 때 필수 선택지가 없으면 400을 반환한다.")
    void testCreateOptionWithoutRequiredChoice() throws Exception{
        MenuCreateReqDTO menuCreateReqDTO = new MenuCreateReqDTO("New Menu", "New Menu Description", 1000, 101L, "image.jpg");
        String MenuId = mockMvc.perform(post("/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateReqDTO))).andReturn().getResponse().getContentAsString();

        OptionCreateReqDTO optionCreateReqDTO = new OptionCreateReqDTO("샷 추가", null, false, Long.parseLong(MenuId));
        mockMvc.perform(post("/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionCreateReqDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("옵션을 수정할때 모든 필드가 존재하는 경우 수정된다.")
    void testUpdateOption() throws Exception{
        MenuCreateReqDTO menuCreateReqDTO = new MenuCreateReqDTO("New Menu", "New Menu Description", 1000, 101L, "image.jpg");
        String MenuId = mockMvc.perform(post("/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateReqDTO))).andReturn().getResponse().getContentAsString();

        OptionCreateReqDTO optionCreateReqDTO = new OptionCreateReqDTO("new 옵션 추가", false, true, Long.parseLong(MenuId));
        String res = mockMvc.perform(post("/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionCreateReqDTO))).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(res);
        Long optionId = jsonObject.getJSONObject("data").getLong("id");

        OptionUpdateReqDTO optionUpdateReqDTO = new OptionUpdateReqDTO(optionId,"샷 추가", true, false);
        mockMvc.perform(put("/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionUpdateReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(optionId));
    }

    @Test
    @DisplayName("옵션을 수정할때 일부 필드만 존재하는 경우 나머지 필드는 기존 값으로 유지된다.")
    void testUpdateOptionWithPartialFields() throws Exception{
        MenuCreateReqDTO menuCreateReqDTO = new MenuCreateReqDTO("New Menu", "New Menu Description", 1000, 101L, "image.jpg");
        String MenuId = mockMvc.perform(post("/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateReqDTO))).andReturn().getResponse().getContentAsString();

        OptionCreateReqDTO optionCreateReqDTO = new OptionCreateReqDTO("new 옵션 추가", false, true, Long.parseLong(MenuId));
        String res = mockMvc.perform(post("/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionCreateReqDTO))).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(res);
        Long optionId = jsonObject.getJSONObject("data").getLong("id");

        OptionUpdateReqDTO optionUpdateReqDTO = new OptionUpdateReqDTO(optionId,null,null, false);
        mockMvc.perform(put("/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionUpdateReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(optionId));
    }

    @Test
    @DisplayName("존재하지 않는 옵션을 수정하려고 하면 404를 반환한다.")
    void testUpdateOptionWithNonExistingOption() throws Exception{
        Long optionId = 100L;
        OptionUpdateReqDTO optionUpdateReqDTO = new OptionUpdateReqDTO(optionId,null,null, false);
        mockMvc.perform(put("/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionUpdateReqDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("옵션을 수정할 때 필수 선택지가 없으면 400을 반환한다.")
    void testUpdateOptionWithoutRequiredChoice() throws Exception{
        OptionUpdateReqDTO optionUpdateReqDTO = new OptionUpdateReqDTO(null,null,null, false);
        mockMvc.perform(put("/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionUpdateReqDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("옵션을 삭제한다")
    void testDeleteOption() throws Exception{
        MenuCreateReqDTO menuCreateReqDTO = new MenuCreateReqDTO("New Menu", "New Menu Description", 1000, 101L, "image.jpg");
        String MenuId = mockMvc.perform(post("/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateReqDTO))).andReturn().getResponse().getContentAsString();

        OptionCreateReqDTO optionCreateReqDTO = new OptionCreateReqDTO("new 옵션 추가", false, true, Long.parseLong(MenuId));
        String res = mockMvc.perform(post("/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionCreateReqDTO))).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(res);
        Long optionId = jsonObject.getJSONObject("data").getLong("id");

        mockMvc.perform(delete("/options/" + optionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(optionId));
    }

    @Test
    @DisplayName("존재하지 않는 옵션을 삭제하려고 하면 404를 반환한다.")
    void testDeleteNonExistingOption() throws Exception{
        Long optionId = 100L;
        mockMvc.perform(delete("/options/" + optionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
