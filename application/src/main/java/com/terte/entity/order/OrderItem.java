package com.terte.entity.order;

import com.terte.entity.menu.Menu;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuId;

    @NotNull
    @Column(nullable = false)
    private Integer quantity;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "orderItem")
    private List<SelectedOption> selectedOptions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public void addSelectedOption(SelectedOption selectedOption) {
        this.selectedOptions.add(selectedOption);
        selectedOption.setOrderItem(this);
    }

    public void removeSelectedOption(SelectedOption selectedOption) {
        this.selectedOptions.remove(selectedOption);
        selectedOption.setOrderItem(null);
    }
}
