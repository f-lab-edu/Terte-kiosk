package com.terte.service.menu;

import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Option;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OptionService {
    Option getOptionById(Long id);
    Option createOption(Option option);
    Option updateOption(Option option);
    void deleteOption(Long id);
    List<Choice> getChoicesById(Long id);
}
