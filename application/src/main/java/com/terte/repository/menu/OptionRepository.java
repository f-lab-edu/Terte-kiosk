package com.terte.repository.menu;

import com.terte.entity.menu.Option;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OptionRepository {
    private final Map<Long, Option> optionStorage = new HashMap<>();

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
