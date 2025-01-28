package com.terte.dto.menu;

import com.terte.entity.category.Category;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResDTO {
    private Long id;
    private String name;
    private String description;

    public static CategoryResDTO from(Category category){
        return new CategoryResDTO(category.getId(), category.getName(), category.getDescription());
    }


}
