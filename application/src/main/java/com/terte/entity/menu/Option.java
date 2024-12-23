package com.terte.entity.menu;

import com.terte.dto.menu.OptionDTO;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Option {
    private Long id;
    private String name;
    private Boolean multipleSelection;
    private Boolean required;
    private List<Choice> choices;

    public OptionDTO toOptionDTO() {
        return new OptionDTO(this.name,this.multipleSelection, this.required,this.choices.stream().map(Choice::toChoiceDTO).collect(Collectors.toList()));
    }

}
