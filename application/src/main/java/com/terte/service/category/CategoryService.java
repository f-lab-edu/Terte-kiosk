package com.terte.service.category;

import com.terte.entity.category.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getAllCategories(Long storeId);
    Category getCategoryById(Long id);
    Category createCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(Long id);
}
