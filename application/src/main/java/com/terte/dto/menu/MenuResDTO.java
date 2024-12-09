package com.terte.dto.menu;

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
    private String category;
    private String image;
}
