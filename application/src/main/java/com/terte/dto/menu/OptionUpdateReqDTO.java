package com.terte.dto.menu;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OptionUpdateReqDTO {
    @NotNull
    private Long id;
    private String name;
    private Boolean multipleSelection;
    private Boolean required;
}
