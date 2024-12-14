package com.terte.dto.menu;

import com.terte.common.enums.MenuCategory;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResDTO {
    private Long id;
    private String name;
    private String description;
}
