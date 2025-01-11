package com.terte.entity.order;

import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelectedOption {
    private Long id;
    private Option option;
    private List<Choice> selectedChoices;
}
