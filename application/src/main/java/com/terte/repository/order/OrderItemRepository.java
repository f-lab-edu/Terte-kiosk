package com.terte.repository.order;

import com.terte.entity.category.Category;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.Option;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderItemRepository {

    private final Map<Long, OrderItem> orderItemStorage = new ConcurrentHashMap<>();

    OrderItemRepository(){
        Category category1 = new Category(101L, "음료", 1L,"음료설명");
        Choice choice1 = new Choice(1L, "샷 추가", 500);
        Option option1 = new Option(1L, "음료 옵션", false, false, List.of(choice1));
        Menu menu1 = new Menu(1L, "아메리카노", 5000, category1, 1L, "https://image.com", "커피", List.of(option1));

        SelectedOption selectedOption1 = new SelectedOption(1L, option1, List.of(choice1));
        OrderItem orderItem1 = new OrderItem(1L,menu1,1,List.of(selectedOption1));

        orderItemStorage.put(orderItem1.getId(), orderItem1);
    }

    public OrderItem findById(Long id) {
        return orderItemStorage.get(id);
    }

    public OrderItem save(OrderItem orderItem) {
        orderItemStorage.put(orderItem.getId(), orderItem);
        return orderItem;
    }
}
