package com.terte.dto.menu;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryReqDTO {
    @NotNull
    private String name;
    private String description;
}
