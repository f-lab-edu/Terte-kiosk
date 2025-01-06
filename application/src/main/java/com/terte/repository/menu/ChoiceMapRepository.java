package com.terte.repository.menu;

import com.terte.entity.menu.Choice;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ChoiceMapRepository {

    public ChoiceMapRepository(){
        // 초기 데이터 설정
        choiceStorage.put(1L, new Choice(1L, "샷 추가", 500,null));
        choiceStorage.put(2L, new Choice(2L, "시럽 추가", 500,null));
        choiceStorage.put(3L, new Choice(3L, "얼음 추가", 0,null));
        choiceStorage.put(4L, new Choice(4L, "얼음 빼기", 0,null));
    }
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
