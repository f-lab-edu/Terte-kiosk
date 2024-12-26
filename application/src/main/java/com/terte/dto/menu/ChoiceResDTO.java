package com.terte.dto.menu;

import com.terte.entity.menu.Choice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceResDTO {
    private Long id;
    private String name;
    private Integer price;

    public static ChoiceResDTO from(Choice choice) {
        return new ChoiceResDTO(choice.getId(),choice.getName(),choice.getPrice());
    }
}
