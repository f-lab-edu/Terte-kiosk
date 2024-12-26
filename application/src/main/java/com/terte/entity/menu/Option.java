package com.terte.entity.menu;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Option {
    private Long id;
    private String name;
    private Boolean multipleSelection;
    private Boolean required;
    private List<Choice> choices;
}
