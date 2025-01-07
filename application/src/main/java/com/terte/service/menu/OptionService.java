package com.terte.service.menu;

import com.terte.entity.menu.Choice;
import com.terte.entity.menu.MenuOption;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OptionService {
    MenuOption getOptionById(Long id);
    MenuOption createOption(MenuOption menuOption);
    MenuOption updateOption(MenuOption menuOption);
    void deleteOption(Long id);
    List<Choice> getChoicesById(Long id);
}
