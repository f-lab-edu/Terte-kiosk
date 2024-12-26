package com.terte.entity.category;

import com.terte.dto.menu.CategoryResDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {
    private Long id;
    private String name;
    private Long storeId;
    private String description;

}
