package com.terte.dto.menu;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceCreateReqDTO {
    @NotNull
    private String name;
    @NotNull
    private Integer price;
    @NotNull
    private Long optionId;
}
