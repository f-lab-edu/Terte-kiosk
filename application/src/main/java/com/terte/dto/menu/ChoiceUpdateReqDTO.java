package com.terte.dto.menu;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceUpdateReqDTO {
    @NotNull
    private Long id;
    private String name;
    private Integer price;
}
