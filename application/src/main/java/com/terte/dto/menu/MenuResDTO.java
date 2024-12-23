package com.terte.dto.menu;

import com.terte.common.enums.MenuCategory;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResDTO {
    private Long id;
    private String name;
    private int price;
    private Long categoryId;
    private String categoryName;
    private String image;
}
