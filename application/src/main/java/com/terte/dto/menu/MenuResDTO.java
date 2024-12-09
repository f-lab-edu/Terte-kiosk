package com.terte.dto.menu;

import com.terte.common.enums.MenuCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResDTO {
    private Long id;
    private String name;
    private int price;
    private MenuCategory category;
    private String image;
}
