package com.terte.dto.menu;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCategoryReqDTO {
    private Long id;
    private String name;
    private String description;
}
