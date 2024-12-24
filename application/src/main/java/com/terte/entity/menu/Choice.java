package com.terte.entity.menu;


import com.terte.dto.menu.ChoiceDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Choice {
    private Long id;
    private String name;
    private int price;

    public ChoiceDTO toChoiceDTO() {
        return new ChoiceDTO(this.name, this.price);
    }
}
