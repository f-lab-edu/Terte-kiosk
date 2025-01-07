package com.terte.repository.order;

import com.terte.entity.menu.Choice;
import com.terte.entity.menu.MenuOption;
import com.terte.entity.order.SelectedOption;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SelectedOptionMapRepository {
    private final Map<Long, SelectedOption> selectedOptionStorage = new ConcurrentHashMap<>();

    SelectedOptionMapRepository(){
        Choice choice1 = new Choice(1L, "샷 추가", 500,null);
        MenuOption menuOption1 = new MenuOption(1L, "음료 옵션", false, false, List.of(choice1),null);
        SelectedOption selectedOption1 = new SelectedOption(1L, menuOption1, List.of(choice1.getId()));

        selectedOptionStorage.put(selectedOption1.getId(), selectedOption1);
    }
    public SelectedOption findById(Long id) {
        return selectedOptionStorage.get(id);
    }
    public SelectedOption save(SelectedOption selectedOption) {
        selectedOptionStorage.put(selectedOption.getId(), selectedOption);
        return selectedOption;
    }

}
