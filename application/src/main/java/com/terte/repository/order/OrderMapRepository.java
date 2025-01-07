package com.terte.repository.order;

import com.terte.common.enums.OrderStatus;
import com.terte.common.enums.OrderType;
import com.terte.entity.category.Category;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.MenuOption;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class OrderMapRepository {
    private final Map<Long, Order> orderStorage = new ConcurrentHashMap<>();

    OrderMapRepository() {
        // 초기 데이터 설정
        Category category1 = new Category(101L, "음료", 1L,"음료설명");
        Choice choice1 = new Choice(1L, "샷 추가", 500,null);
        MenuOption menuOption1 = new MenuOption(1L, "음료 옵션", false, false, List.of(choice1),null);
        Menu menu1 = new Menu(1L, "아메리카노", 5000, category1, 1L, "https://image.com", "커피", List.of(menuOption1));

        SelectedOption selectedOption1 = new SelectedOption(1L, menuOption1, List.of(choice1.getId()));
        OrderItem orderItem1 = new OrderItem(1L,menu1,1,List.of(selectedOption1));

        Order order1 = new Order(1L, 1L, OrderStatus.ORDERED, List.of(orderItem1), OrderType.EATIN, "01012345678",1);
        orderStorage.put(order1.getId(), order1);

    }
    public List<Order> findByStoreId(Long storeId) {
        return orderStorage.values().stream().filter(order -> order.getStoreId().equals(storeId)).collect(Collectors.toList());
    }
    public Order findById(Long id) {
        return orderStorage.get(id);
    }

    public Order save(Order order) {
        if(order.getId() == null) {
            order.setId(orderStorage.size() + 1L);
            orderStorage.put(order.getId(), order);
        } else {
            orderStorage.put(order.getId(), order);
        }
        return order;
    }

    public void deleteById(Long id) {
        orderStorage.remove(id);
    }
}
