package com.terte.service.menu;

import com.terte.common.exception.NotFoundException;
import com.terte.dto.menu.OptionResDTO;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Option;
import com.terte.repository.menu.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;

    @Override
    public Option createOption(Option option) {
        return optionRepository.save(option);
    }

    @Override
    public Option updateOption(Option option) {
        Option existingOption = optionRepository.findById(option.getId());
        if(existingOption == null){
            throw new NotFoundException("Option Not Found");
        }
        if(option.getName() == null){
            option.setName(existingOption.getName());
        }
        if(option.getRequired() == null){
            option.setRequired(existingOption.getRequired());
        }
        if(option.getMultipleSelection() == null){
            option.setMultipleSelection(existingOption.getMultipleSelection());
        }
        return optionRepository.save(option);
    }

    @Override
    public void deleteOption(Long id) {
        Option existingOption = optionRepository.findById(id);
        if(existingOption == null){
            throw new NotFoundException("Option Not Found");
        }
        optionRepository.deleteById(id);
    }

    @Override
    public List<Choice> getChoicesById(Long id) {
        Option existingOption = optionRepository.findById(id);
        if(existingOption == null){
            throw new NotFoundException("Option Not Found");
        }
        return existingOption.getChoices();
    }
}
