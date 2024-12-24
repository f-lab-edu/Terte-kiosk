package com.terte.entity.category;

import com.terte.dto.menu.CategoryResDTO;
import com.terte.dto.menu.CreateCategoryReqDTO;
import com.terte.dto.menu.UpdateCategoryReqDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {
    private Long id;
    private String name;
    private Long storeId;

    public CategoryResDTO toCategoryResDTO() {
        return new CategoryResDTO(this.id, this.name);
    }
}
