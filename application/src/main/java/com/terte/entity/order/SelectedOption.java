package com.terte.entity.order;

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

    private Long menuOptionId;

    @ElementCollection
    @CollectionTable(name = "selected_option_choice_ids", joinColumns = @JoinColumn(name = "selected_option_id"))
    private List<Long> selectedChoiceIds;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
}
