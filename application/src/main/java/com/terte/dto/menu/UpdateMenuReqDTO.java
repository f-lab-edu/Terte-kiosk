package com.terte.dto.menu;

import com.terte.common.enums.MenuCategory;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMenuReqDTO {
    private Long id;
    private String name;
    private String description;
    private int price;
    private MenuCategory category;
    private String image;
    private List<OptionDTO> options;
}
