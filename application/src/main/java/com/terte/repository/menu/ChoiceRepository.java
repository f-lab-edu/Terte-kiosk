package com.terte.repository.menu;

import com.terte.entity.menu.Choice;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ChoiceRepository {
    private final Map<Long, Choice> choiceStorage = new ConcurrentHashMap<>();

    public Choice findById(Long id) {
        return choiceStorage.get(id);
    }

    public Choice save(Choice choice) {
        if(choice.getId() == null) {
            choice.setId(choiceStorage.size() + 1L);
            choiceStorage.put(choice.getId(), choice);
        } else {
            choiceStorage.put(choice.getId(), choice);
        }
        return choice;
    }

    public void deleteById(Long id) {
        choiceStorage.remove(id);
    }
}
