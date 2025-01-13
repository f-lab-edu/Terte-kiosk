package com.terte.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terte.TerteMainApplication;
import com.terte.dto.menu.ChoiceCreateReqDTO;
import com.terte.dto.menu.ChoiceUpdateReqDTO;
import com.terte.dto.menu.MenuCreateReqDTO;
import com.terte.dto.menu.OptionCreateReqDTO;
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
public class ChoiceControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("선택지를 생성한다.")
    void testCreateChoice() throws Exception {
        ChoiceCreateReqDTO choiceCreateReqDTO = new ChoiceCreateReqDTO("샷 추가", 500,1L);
        mockMvc.perform(post("/choices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(choiceCreateReqDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(6L));
    }

    @Test
    @DisplayName("선택지를 수정한다.")
    void testUpdateChoice() throws Exception {
        MenuCreateReqDTO menuCreateReqDTO = new MenuCreateReqDTO("New Menu", "New Menu Description", 1000, 101L, "image.jpg");
        String MenuId = mockMvc.perform(post("/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateReqDTO))).andReturn().getResponse().getContentAsString();

        OptionCreateReqDTO optionCreateReqDTO = new OptionCreateReqDTO("샷 추가", false, true, Long.parseLong(MenuId));
        String optionId = mockMvc.perform(post("/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionCreateReqDTO))).andReturn().getResponse().getContentAsString();

        ChoiceCreateReqDTO choiceCreateReqDTO = new ChoiceCreateReqDTO("샷 추가", 500, Long.parseLong(optionId));
        String res = mockMvc.perform(post("/choices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(choiceCreateReqDTO))
                ).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(res);
        Long choiceId = jsonObject.getJSONObject("data").getLong("id");

        ChoiceUpdateReqDTO choiceUpdateReqDTO = new ChoiceUpdateReqDTO(choiceId, null, 1000);
        mockMvc.perform(put("/choices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(choiceUpdateReqDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(choiceId));
    }

    @Test
    @DisplayName("존재하지 않는 선택지를 수정하려고 하면 404를 반환한다.")
    void testUpdateChoiceWithNonExistingChoice() throws  Exception {
        ChoiceUpdateReqDTO choiceUpdateReqDTO = new ChoiceUpdateReqDTO(100L, "샷 추가", 1000);
        mockMvc.perform(put("/choices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(choiceUpdateReqDTO))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("선택지를 삭제한다.")
    void testDeleteChoice() throws Exception {
        MenuCreateReqDTO menuCreateReqDTO = new MenuCreateReqDTO("New Menu", "New Menu Description", 1000, 101L, "image.jpg");
        String MenuId = mockMvc.perform(post("/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateReqDTO))).andReturn().getResponse().getContentAsString();

        OptionCreateReqDTO optionCreateReqDTO = new OptionCreateReqDTO("샷 추가", false, true, Long.parseLong(MenuId));
        String optionId = mockMvc.perform(post("/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optionCreateReqDTO))).andReturn().getResponse().getContentAsString();

        ChoiceCreateReqDTO choiceCreateReqDTO = new ChoiceCreateReqDTO("샷 추가", 500, Long.parseLong(optionId));
        String res = mockMvc.perform(post("/choices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(choiceCreateReqDTO))
                ).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(res);
        Long choiceId = jsonObject.getJSONObject("data").getLong("id");

        mockMvc.perform(delete("/choices/" + choiceId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(choiceId));
    }

    @Test
    @DisplayName("존재하지 않는 선택지를 삭제하려고 하면 404를 반환한다.")
    void testDeleteNonExistingChoice() throws Exception {
        mockMvc.perform(delete("/choices/100")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

}
