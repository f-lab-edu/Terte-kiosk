package com.terte.repository.menu;

import com.terte.entity.category.Category;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.MenuOption;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class MenuMapRepository {
    private final Map<Long, Menu> menuStorage = new ConcurrentHashMap<>();

    public MenuMapRepository() {
        // 초기 데이터 설정
        Category category1 = new Category(101L, "음료", 1L,"음료설명");
        Category category2 = new Category(102L, "빙수", 1L,"빙수설명");

        Choice choice1 = new Choice(1L, "샷 추가", 500,null);
        Choice choice2 = new Choice(2L, "시럽 추가", 500,null);
        Choice choice3 = new Choice(3L, "얼음 추가", 0,null);
        Choice choice4 = new Choice(4L, "얼음 빼기", 0,null);

        MenuOption menuOption1 = new MenuOption(1L, "음료 옵션", false, false, List.of(choice1, choice2),null);
        MenuOption menuOption2 = new MenuOption(2L, "빙수 옵션", true, true, List.of(choice3, choice4),null);

        menuStorage.put(1L, new Menu(1L, "아메리카노", 5000, category1, 1L, "https://image.com", "커피", List.of(menuOption1)));
        menuStorage.put(2L, new Menu(2L, "카페라떼", 6000, category1, 1L, "https://image.com", "우유", List.of(menuOption1)));
        menuStorage.put(3L, new Menu(3L, "아이스 아메리카노", 5500, category1, 1L, "https://image.com", "커피", List.of(menuOption1)));
    }

    public List<Menu> findByStoreId(Long storeId) {
        return menuStorage.values().stream().filter(menu -> menu.getStoreId().equals(storeId)).collect(Collectors.toList());
    }

    public List<Menu> findByCategory(Category category) {
        return menuStorage.values().stream().filter(menu -> menu.getCategory().getId().equals(category.getId())).collect(Collectors.toList());
    }

    public Menu findById(Long id) {
        return menuStorage.get(id);
    }

    public Menu save(Menu menu) {
        if(menu.getId() == null){
            menu.setId(menuStorage.size() + 1L);
            menuStorage.put(menu.getId(), menu);
            return menu;
        } else {
            menuStorage.put(menu.getId(), menu);
            return menu;
        }
    }

    public void deleteById(Long id) {
        menuStorage.remove(id);
    }

}
