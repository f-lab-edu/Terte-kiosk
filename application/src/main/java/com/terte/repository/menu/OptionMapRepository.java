package com.terte.repository.menu;

import com.terte.entity.menu.Choice;
import com.terte.entity.menu.MenuOption;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OptionMapRepository {

    public OptionMapRepository() {
        // 초기 데이터 설정
        Choice choice1 = new Choice(1L, "샷 추가", 500,null);
        Choice choice2 = new Choice(2L, "시럽 추가", 500,null);
        Choice choice3 = new Choice(3L, "얼음 추가", 0,null);
        Choice choice4 = new Choice(4L, "얼음 빼기", 0,null);

        MenuOption menuOption1 = new MenuOption(1L, "음료 옵션", false, false, List.of(choice1, choice2),null);
        MenuOption menuOption2 = new MenuOption(2L, "빙수 옵션", true, true, List.of(choice3, choice4),null);

        optionStorage.put(1L, menuOption1);
        optionStorage.put(2L, menuOption2);
    }
    private final Map<Long, MenuOption> optionStorage = new ConcurrentHashMap<>();

    public MenuOption findById(Long id) {
        return optionStorage.get(id);
    }

    public MenuOption save(MenuOption menuOption) {
        if(menuOption.getId() == null) {
            menuOption.setId(optionStorage.size() + 1L);
            optionStorage.put(menuOption.getId(), menuOption);
        } else {
            optionStorage.put(menuOption.getId(), menuOption);
        }
        return menuOption;
    }

    public void deleteById(Long id) {
        optionStorage.remove(id);
    }
}
