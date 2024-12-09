package com.terte.dto.menu;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OptionDTO {
    private String name;
    private boolean multipleSelection;
    private boolean required;
    private List<ChoiceDTO> choices;
}
