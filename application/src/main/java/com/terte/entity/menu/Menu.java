package com.terte.entity.menu;

import com.terte.entity.category.Category;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Menu {
    private Long id;
    private String name;
    private Integer price;
    private Category category;
    private Long storeId;
    private String image;
    private String description;
    private List<Option> options;

}


