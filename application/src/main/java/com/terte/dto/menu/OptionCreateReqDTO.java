package com.terte.dto.menu;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OptionCreateReqDTO {
    @NotNull
    private String name;
    @NotNull
    private Boolean multipleSelection;
    @NotNull
    private Boolean required;
}
