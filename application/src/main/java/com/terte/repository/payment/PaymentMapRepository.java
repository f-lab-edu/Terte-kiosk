package com.terte.repository.payment;

import com.terte.common.enums.*;
import com.terte.entity.category.Category;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.MenuOption;
import com.terte.entity.order.Order;
import com.terte.entity.order.OrderItem;
import com.terte.entity.order.SelectedOption;
import com.terte.entity.payment.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class PaymentMapRepository {
    private final Map<Long, Payment> paymentStorage = new ConcurrentHashMap<>();

    PaymentMapRepository() {
        // 초기 데이터 설정
        Category category1 = new Category(101L, "음료", 1L,"음료설명");
        Choice choice1 = new Choice(1L, "샷 추가", 500,null);
        MenuOption menuOption1 = new MenuOption(1L, "음료 옵션", false, false, List.of(choice1),null);
        Menu menu1 = new Menu(1L, "아메리카노", 5000, category1, 1L, "https://image.com", "커피", List.of(menuOption1));

        SelectedOption selectedOption1 = new SelectedOption(1L, menuOption1, List.of(choice1.getId()));
        OrderItem orderItem1 = new OrderItem(1L,menu1,1,List.of(selectedOption1));

        Order order1 = new Order(1L, 1L, OrderStatus.ORDERED, List.of(orderItem1), OrderType.EATIN, "01012345678",1);
        Payment payment1 = new Payment(1L, 1L, PaymentMethod.CASH, PaymentStatus.PAYMENT_COMPLETED,order1);
        paymentStorage.put(payment1.getId(), payment1);
    }

    public List<Payment> findByStoreId(Long storeId) {
        return paymentStorage.values().stream().filter(payment -> payment.getStoreId().equals(storeId)).collect(Collectors.toList());
    }

    public Payment findById(Long id) {
        return paymentStorage.get(id);
    }

    public Payment save(Payment payment) {
        if(payment.getId() == null) {
            payment.setId(paymentStorage.size() + 1L);
            paymentStorage.put(payment.getId(), payment);
        } else {
            paymentStorage.put(payment.getId(), payment);
        }
        return payment;
    }

    public void deleteById(Long id) {
        paymentStorage.remove(id);
    }
}
