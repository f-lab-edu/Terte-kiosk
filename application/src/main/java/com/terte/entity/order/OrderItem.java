package com.terte.entity.order;

import com.terte.entity.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Long id;
    private Menu menu;
    private Integer quantity;
    private List<SelectedOption> selectedOptions;
}
