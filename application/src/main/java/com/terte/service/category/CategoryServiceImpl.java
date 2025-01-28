package com.terte.service.category;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.category.Category;
import com.terte.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    //private final CategoryMapRepository categoryRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories(Long StoreId) {
        List<Category> categories = categoryRepository.findByStoreId(StoreId);
        if(categories.isEmpty()){
            throw new NotFoundException("Category not found");
        }
        return categories;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        Category existionCategory = categoryRepository.findById(category.getId()).orElseThrow(() -> new NotFoundException("Category not found"));
        if (category.getName() == null){
            category.setName(existionCategory.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.deleteById(id);
    }
}
