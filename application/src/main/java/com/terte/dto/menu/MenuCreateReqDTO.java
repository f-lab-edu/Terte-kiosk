package com.terte.dto.menu;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuCreateReqDTO {
    @NotNull
    private String name;
    private String description;
    @Min(0)
    private int price;
    @Min(1)
    private Long categoryId;
    private String image;
}
