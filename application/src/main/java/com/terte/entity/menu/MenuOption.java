package com.terte.entity.menu;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "menu_option")
public class MenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Boolean multipleSelection;
    @NotNull
    private Boolean required;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "menuOption")
    private List<Choice> choices;
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
}
