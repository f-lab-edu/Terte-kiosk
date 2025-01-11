package com.terte.dto.menu;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuUpdateReqDTO {
    @NotNull
    private Long id;
    private String name;
    private String description;
    private int price;
    private Long categoryId;
    private String image;
}
