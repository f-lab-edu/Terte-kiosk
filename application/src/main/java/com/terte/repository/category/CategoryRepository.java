package com.terte.repository.category;

import com.terte.entity.category.Category;
import com.terte.entity.menu.Menu;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class CategoryRepository {
    private final Map<Long, Category> categoryStorage = new ConcurrentHashMap<>();

    public CategoryRepository() {
        // 초기 데이터 설정
        Category category1 = new Category(101L, "음료", 1L, "음료설명");
        Category category2 = new Category(102L, "빙수", 1L, "빙수설명");
        categoryStorage.put(101L, category1);
        categoryStorage.put(102L, category2);
    }

    public void init(){
        categoryStorage.clear();
        Category category1 = new Category(101L, "음료", 1L, "음료설명");
        Category category2 = new Category(102L, "빙수", 1L, "빙수설명");
        categoryStorage.put(101L, category1);
        categoryStorage.put(102L, category2);
    }

    public List<Category> findByStoreId(Long storeId) {
        return categoryStorage.values().stream().filter(category -> category.getStoreId().equals(storeId)).collect(Collectors.toList());
    }

    public Category findById(Long id) {
        return categoryStorage.get(id);
    }

    public Category save(Category category) {
        category.setId(categoryStorage.size() + 1L);
        categoryStorage.put(category.getId(), category);
        return category;
    }

    public Category update(Category category) {
        categoryStorage.put(category.getId(), category);
        return category;
    }

    public void deleteById(Long id) {
        categoryStorage.remove(id);
    }
}
