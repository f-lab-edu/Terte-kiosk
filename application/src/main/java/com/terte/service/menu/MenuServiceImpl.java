package com.terte.service.menu;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.category.Category;
import com.terte.entity.menu.Choice;
import com.terte.entity.menu.Menu;
import com.terte.entity.menu.Option;
import com.terte.repository.menu.*;
import com.terte.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    //private final MenuMapRepository menuRepository;
    private final MenuRepository menuRepository;
    private final CategoryService categoryService;

    @Override
    public List<Menu> getAllMenus(Long storeId, Long categoryId) {

        if(categoryId == null){
            return menuRepository.findByStoreId(storeId).orElseThrow(() -> new NotFoundException("Menu not found"));
        } else {
            return menuRepository.findByCategoryId(categoryId).orElseThrow(() -> new NotFoundException("Menu not found"));
        }
    }

    @Override
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElseThrow(() -> new NotFoundException("Menu not found"));
    }

    @Override
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public Menu updateMenu(Menu menu) {
        Menu existingMenu = menuRepository.findById(menu.getId()).orElseThrow(() -> new NotFoundException("Menu not found"));
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

        return menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        menuRepository.findById(id).orElseThrow(() -> new NotFoundException("Menu not found"));
        menuRepository.deleteById(id);
    }


    public List<Option> getOptionsById(Long id){
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new NotFoundException("Menu not found"));
        return menu.getOptions();
    }

}
