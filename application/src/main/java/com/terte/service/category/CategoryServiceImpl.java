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

    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories(Long StoreId) {
        return categoryRepository.findByStoreId(StoreId);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        Category createdCategory = categoryRepository.save(category);
        return createdCategory;
    }

    @Override
    public Category updateCategory(Category category) {
        Category existionCategory = categoryRepository.findById(category.getId());
        if(existionCategory == null){
            throw new NotFoundException("Category not found");
        }
        if (category.getName() == null){
            category.setName(existionCategory.getName());
        }
        return categoryRepository.update(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category existionCategory = categoryRepository.findById(id);
        if(existionCategory == null){
            throw new NotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
