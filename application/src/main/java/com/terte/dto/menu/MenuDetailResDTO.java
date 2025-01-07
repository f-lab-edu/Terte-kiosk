package com.terte.dto.menu;

import com.terte.entity.menu.Menu;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDetailResDTO {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Long categoryId;
    private String categoryName;
    private String image;
    private List<OptionResDTO> options;

    public static MenuDetailResDTO from(Menu menu){
        List<OptionResDTO> optionResDTOList = menu.getMenuOptions().stream().map(OptionResDTO::from).collect(Collectors.toList());
        return new MenuDetailResDTO(menu.getId(), menu.getName(), menu.getDescription(), menu.getPrice(), menu.getCategory().getId(), menu.getCategory().getName(), menu.getImage(), optionResDTOList);
    }
}
