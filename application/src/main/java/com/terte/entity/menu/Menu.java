package com.terte.entity.menu;

import com.terte.entity.category.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    private Long storeId;
    private String image;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "menu")
    private List<MenuOption> menuOptions;
}


