package com.terte.entity.order;

import com.terte.entity.menu.MenuOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SelectedOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_option_id", nullable = false)
    private MenuOption menuOption;

    @ElementCollection
    private List<Long> selectedChoiceIds;
}
