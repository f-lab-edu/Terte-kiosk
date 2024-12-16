package com.terte.dto.menu;

import com.terte.common.enums.MenuCategory;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDetailResDTO {
    private Long id;
    private String name;
    private String description;
    private int price;
    private Long categoryId;
    private String categoryName;
    private String image;
    private List<OptionDTO> options; // 메뉴에 종속된 옵션 리스트
}
