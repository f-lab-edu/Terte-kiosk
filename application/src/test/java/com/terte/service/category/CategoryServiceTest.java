package com.terte.service.category;

import com.terte.common.exception.NotFoundException;
import com.terte.entity.category.Category;
import com.terte.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories() {
        Long storeId = 1L;
        List<Category> categories = Arrays.asList(new Category(1L, "음료", 1L), new Category(2L, "빙수", 1L));
        when(categoryRepository.findByStoreId(storeId)).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories(storeId);

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findByStoreId(storeId);
    }

    @Test
    void getCategoryById() {
        Long categoryId = 1L;
        Category category = new Category(categoryId, "음료", 1L);
        when(categoryRepository.findById(categoryId)).thenReturn(category);

        Category result = categoryService.getCategoryById(categoryId);

        assertNotNull(result);
        assertEquals("음료", result.getName());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void createCategory() {
        Category category = new Category(null, "Beverages", 1L);
        Category savedCategory = new Category(1L, "Beverages", 1L);
        when(categoryRepository.save(category)).thenReturn(savedCategory);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("카테고리가 존재할 때 정상적으로 업데이트된다")
    void updateCategorySuccess() {
        // given
        Category existingCategory = new Category(1L, "음료",1L);
        Category updatedCategory = new Category(1L, "빙수",1L);

        when(categoryRepository.findById(1L)).thenReturn(existingCategory);
        when(categoryRepository.update(updatedCategory)).thenReturn(updatedCategory);

        // when
        Category result = categoryService.updateCategory(updatedCategory);

        // then
        assertEquals("빙수", result.getName());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).update(updatedCategory);
    }

    @Test
    @DisplayName("카테고리가 존재하지 않을 때 NotFoundException 발생")
    void updateCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(null);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> categoryService.updateCategory(new Category(1L, "빙수",1L))
        );

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository, never()).update(any());
    }

    @Test
    @DisplayName("이름이 없는 카테고리를 업데이트할 때 기존 이름 유지")
    void updateCategoryNameNull() {
        Category existingCategory = new Category(1L, "음료",1L);
        Category updatedCategory = new Category(1L, null,1L);

        when(categoryRepository.findById(1L)).thenReturn(existingCategory);
        when(categoryRepository.update(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = categoryService.updateCategory(updatedCategory);

        assertEquals("음료", result.getName()); // 기존 이름 유지
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).update(updatedCategory);
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategorySuccess() {
        Long categoryId = 1L;
        Category existingCategory = new Category(1L, "음료",1L);
        when(categoryRepository.findById(categoryId)).thenReturn(existingCategory);
        doNothing().when(categoryRepository).deleteById(categoryId);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 삭제 시 NotFoundException 발생")
    void deleteCategoryNotFound() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(null);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> categoryService.deleteCategory(categoryId)
        );

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository, never()).deleteById(categoryId);
    }

}