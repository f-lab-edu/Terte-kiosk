package com.terte.dto.menu;

import com.terte.entity.menu.Option;
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

    public static OptionResDTO from(Option option) {
        List<ChoiceResDTO> choiceResDTOList = option.getChoices().stream().map(ChoiceResDTO::from).collect(Collectors.toList());
        return new OptionResDTO(option.getId(), option.getName(), option.getMultipleSelection(), option.getRequired(), choiceResDTOList);

    }
}
