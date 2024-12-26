package com.terte.entity.menu;

import com.terte.dto.menu.CreateMenuReqDTO;
import com.terte.dto.menu.MenuDetailResDTO;
import com.terte.dto.menu.MenuResDTO;
import com.terte.dto.menu.UpdateMenuReqDTO;
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
    private int price;
    private Category category;
    private Long storeId;
    private String image;
    private String description;
    private List<Option> options;

    public MenuResDTO toMenuResDTO() {
        return new MenuResDTO(this.id, this.name, this.price, this.category.getId(), this.category.getName(), this.image);

    }

    public MenuDetailResDTO toMenuDetailResDTO(){
        return new MenuDetailResDTO(this.id, this.name, this.description, this.price, this.category.getId(), this.category.getName(), this.image, this.options.stream().map(Option::toOptionDTO).toList());
    }

}


