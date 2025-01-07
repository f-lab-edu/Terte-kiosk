package com.terte.dto.menu;

import com.terte.entity.menu.MenuOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OptionResDTO {
    private Long id;
    private String name;
    private Boolean multipleSelection;
    private Boolean required;
    private List<ChoiceResDTO> choices;

    public static OptionResDTO from(MenuOption menuOption) {
        List<ChoiceResDTO> choiceResDTOList = menuOption.getChoices().stream().map(ChoiceResDTO::from).collect(Collectors.toList());
        return new OptionResDTO(menuOption.getId(), menuOption.getName(), menuOption.getMultipleSelection(), menuOption.getRequired(), choiceResDTOList);

    }
}
