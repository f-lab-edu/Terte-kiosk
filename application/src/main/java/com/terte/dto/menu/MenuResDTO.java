package com.terte.dto.menu;

import com.terte.common.enums.MenuCategory;
import com.terte.entity.menu.Menu;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResDTO {
    private Long id;
    private String name;
    private Integer price;
    private Long categoryId;
    private String categoryName;
    private String image;

    public static MenuResDTO from(Menu menu){
        return new MenuResDTO(menu.getId(), menu.getName(),menu.getPrice(), menu.getCategory().getId(),menu.getCategory().getName(), menu.getImage());
    }
}
