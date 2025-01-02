package com.terte.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SelectedOptionDTO {
    private Long optionId;
    private List<Long> selectedChoiceIds;
}
