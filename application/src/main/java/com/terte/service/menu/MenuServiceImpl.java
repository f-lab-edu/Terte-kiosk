package com.terte.service.menu;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.category.Category;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.Option;
import com.terte.repository.menu.ChoiceRepository;
import com.terte.repository.menu.MenuRepository;
import com.terte.repository.menu.OptionRepository;
import com.terte.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final CategoryService categoryService;
    private final OptionRepository optionRepository;
    private final ChoiceRepository choiceRepository;

    @Override
    public List<Menu> getAllMenus(Long storeId, Long categoryId) {

        if(categoryId == null){
            return menuRepository.findByStoreId(storeId);
        } else {
            Category category = categoryService.getCategoryById(categoryId);
            System.out.println(category.getId());
            return menuRepository.findByCategory(category);
        }
    }

    @Override
    public Menu getMenuById(Long id) {
        Menu existingMenu =  menuRepository.findById(id);
        if (existingMenu == null) {
            throw new NotFoundException("Menu not found");
        }else {
            return existingMenu;
        }
    }

    @Override
    public Menu createMenu(Menu menu) {
        Menu savedMenu = menuRepository.save(menu);

        if (menu.getOptions() != null) {
            menu.getOptions().forEach(option -> {
                Option savedOption = createOption(option);

                if (option.getChoices() != null) {
                    option.getChoices().forEach(this::createChoice);
                }
            });
        }

        return savedMenu;


    }

    @Override
    public Menu updateMenu(Menu menu) {
        Menu existingMenu = menuRepository.findById(menu.getId());
        if (existingMenu == null) {
            throw new NotFoundException("Menu not found");
        }
        if(menu.getDescription() == null){
            menu.setDescription(existingMenu.getDescription());
        }
        if(menu.getImage() == null){
            menu.setImage(existingMenu.getImage());
        }
        if(menu.getName() == null){
            menu.setName(existingMenu.getName());
        }
        if(menu.getPrice() == 0){
            menu.setPrice(existingMenu.getPrice());
        }
        if(menu.getCategory() == null){
            menu.setCategory(existingMenu.getCategory());
        }
        if (menu.getOptions() == null){
            menu.setOptions(existingMenu.getOptions());
        }


       Menu updatedMenu = menuRepository.save(menu);

        if (menu.getOptions() != null) {
            menu.getOptions().forEach(option -> {

                if(option.getId() == null){
                    createOption(option);
                } else {
                    updateOption(option);
                }

                if (option.getChoices() != null) {
                    option.getChoices().forEach(choice -> {
                        if(choice.getId() == null){
                            createChoice(choice);
                        } else {
                            updateChoice(choice);
                        }
                    });
                }
            });
        }

        return updatedMenu;
    }

    @Override
    public void deleteMenu(Long id) {
        Menu existingMenu = menuRepository.findById(id);
        if (existingMenu == null) {
            throw new NotFoundException("Menu not found");
        }
        menuRepository.deleteById(id);

        // 연관된 옵션과 초이스 삭제
        List<Option> options = existingMenu.getOptions();
        if (options != null) {
            options.forEach(option -> {
                optionRepository.deleteById(option.getId());
                List<Choice> choices = option.getChoices();
                if (choices != null) {
                    choices.forEach(choice -> choiceRepository.deleteById(choice.getId()));
                }
            });
        }
    }

    private Choice updateChoice(Choice choice) {
        Choice existingChoice = choiceRepository.findById(choice.getId());
        if(existingChoice == null){
            throw new NotFoundException("Choice not found");
        }
        if(choice.getName() == null){
            choice.setName(existingChoice.getName());
        }
        if(choice.getPrice() == 0){
            choice.setPrice(existingChoice.getPrice());
        }
        return choiceRepository.save(choice);
    }

    private Option updateOption(Option option) {
        Option existingOption = optionRepository.findById(option.getId());
        if(existingOption == null){
            throw new NotFoundException("Option not found");
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
        if(option.getChoices() == null){
            option.setChoices(existingOption.getChoices());
        }
        return optionRepository.save(option);
    }

    private Option createOption(Option option) {
        return optionRepository.save(option);
    }

    private Choice createChoice(Choice choice) {
        return choiceRepository.save(choice);
    }

}