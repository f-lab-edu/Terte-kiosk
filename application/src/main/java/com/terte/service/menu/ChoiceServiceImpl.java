package com.terte.service.menu;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.menu.Choice;
import com.terte.repository.menu.ChoiceMapRepository;
import com.terte.repository.menu.ChoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChoiceServiceImpl implements ChoiceService {
    //private final ChoiceMapRepository choiceMapRepository;
    private final ChoiceRepository choiceRepository;
    @Override
    public Choice getChoiceById(Long id) {
        Choice existingChoice = choiceRepository.findById(id).orElseThrow(() -> new NotFoundException("Choice Not Found"));
        return existingChoice;
    }
    @Override
    public Choice createChoice(Choice choice) {
        return choiceRepository.save(choice);
    }

    @Override
    public Choice updateChoice(Choice choice) {
        Choice existingChoice = choiceRepository.findById(choice.getId()).orElseThrow(() -> new NotFoundException("Choice Not Found"));
        if(choice.getName() == null){
            choice.setName(existingChoice.getName());
        }
        if(choice.getPrice() == null){
            choice.setPrice(existingChoice.getPrice());
        }
        return choiceRepository.save(choice);
    }

    @Override
    public void deleteChoice(Long id) {
        choiceRepository.findById(id).orElseThrow(() -> new NotFoundException("Choice Not Found"));
        choiceRepository.deleteById(id);
    }
}
