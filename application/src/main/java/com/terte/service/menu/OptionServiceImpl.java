package com.terte.service.menu;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.MenuOption;
import com.terte.repository.menu.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    //private final OptionMapRepository optionRepository;
    private final OptionRepository optionRepository;

    @Override
    public MenuOption getOptionById(Long id) {
        return optionRepository.findById(id).orElseThrow(() -> new NotFoundException("Option Not Found"));
    }
    @Override
    public MenuOption createOption(MenuOption menuOption) {
        return optionRepository.save(menuOption);
    }

    @Override
    public MenuOption updateOption(MenuOption menuOption) {
        MenuOption existingMenuOption = optionRepository.findById(menuOption.getId()).orElseThrow(() -> new NotFoundException("Option Not Found"));
        if(menuOption.getName() == null){
            menuOption.setName(existingMenuOption.getName());
        }
        if(menuOption.getRequired() == null){
            menuOption.setRequired(existingMenuOption.getRequired());
        }
        if(menuOption.getMultipleSelection() == null){
            menuOption.setMultipleSelection(existingMenuOption.getMultipleSelection());
        }
        if(menuOption.getChoices() == null){
            menuOption.setChoices(existingMenuOption.getChoices());
        }
        return optionRepository.save(menuOption);
    }

    @Override
    public void deleteOption(Long id) {
        optionRepository.findById(id).orElseThrow(() -> new NotFoundException("Option Not Found"));
        optionRepository.deleteById(id);
    }

    @Override
    public List<Choice> getChoicesById(Long id) {
        MenuOption existingMenuOption = optionRepository.findById(id).orElseThrow(() -> new NotFoundException("Option Not Found"));
        return existingMenuOption.getChoices();
    }
}
