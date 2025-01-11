package com.terte.repository.menu;

import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Option;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OptionRepository {

    public OptionRepository() {
        // 초기 데이터 설정
        Choice choice1 = new Choice(1L, "샷 추가", 500);
        Choice choice2 = new Choice(2L, "시럽 추가", 500);
        Choice choice3 = new Choice(3L, "얼음 추가", 0);
        Choice choice4 = new Choice(4L, "얼음 빼기", 0);

        Option option1 = new Option(1L, "음료 옵션", false, false, List.of(choice1, choice2));
        Option option2 = new Option(2L, "빙수 옵션", true, true, List.of(choice3, choice4));

        optionStorage.put(1L, option1);
        optionStorage.put(2L, option2);
    }
    private final Map<Long, Option> optionStorage = new ConcurrentHashMap<>();

    public Option findById(Long id) {
        return optionStorage.get(id);
    }

    public Option save(Option option) {
        if(option.getId() == null) {
            option.setId(optionStorage.size() + 1L);
            optionStorage.put(option.getId(), option);
        } else {
            optionStorage.put(option.getId(), option);
        }
        return option;
    }

    public void deleteById(Long id) {
        optionStorage.remove(id);
    }
}
