package com.terte.dto.menu;

import com.terte.common.enums.MenuCategory;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResDTO {
    private Long id;
    private String name;
    private String description;

    public CategoryResDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
