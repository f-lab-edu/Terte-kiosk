package com.terte.dto.menu;

import com.terte.common.enums.MenuCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMenuReqDTO {
    @NotNull
    private String name;
    private String description;
    @Min(0)
    private int price;
    @Min(1)
    private Long categoryId;
    private String image;
    private List<OptionDTO> options;
}
