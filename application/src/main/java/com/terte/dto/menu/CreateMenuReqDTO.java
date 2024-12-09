package com.terte.dto.menu;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuReqDTO {
    private String name;
    private String description;
    private int price;
    private String category;
    private String image;
    private List<OptionDTO> options;
}
