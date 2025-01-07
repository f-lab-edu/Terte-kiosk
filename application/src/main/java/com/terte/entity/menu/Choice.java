package com.terte.entity.menu;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Integer price;
    @ManyToOne
    @JoinColumn(name = "menu_option_id", nullable = false)
    MenuOption menuOption;
}
