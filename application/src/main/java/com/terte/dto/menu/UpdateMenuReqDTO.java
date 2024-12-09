package com.terte.dto.menu;

import com.terte.common.enums.MenuCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuReqDTO {
    private Long id;
    private String name;
    private String description;
    private int price;
    private MenuCategory category;
    private String image;
    private List<OptionDTO> options;
}
