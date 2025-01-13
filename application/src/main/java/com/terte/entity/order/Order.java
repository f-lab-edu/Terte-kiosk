package com.terte.entity.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
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
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long storeId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    private String phoneNumber;

    private Integer tableNumber;

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

}
