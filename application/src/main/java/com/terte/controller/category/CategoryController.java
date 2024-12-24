package com.terte.controller.category;

import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.menu.CategoryResDTO;
import com.terte.dto.menu.CreateCategoryReqDTO;
import com.terte.dto.menu.UpdateCategoryReqDTO;
import com.terte.entity.category.Category;
import com.terte.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * GET /categories
     *  카테고리 조회
     */
    @GetMapping
    public ResponseEntity<ApiResDTO<List<CategoryResDTO>>> getCategoryList() {
        //return categoryService.getCategoryList();
        List<Category> categories = categoryService.getAllCategories(1L);
        List<CategoryResDTO> CategoryResDTOList = categories.stream().map(Category::toCategoryResDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResDTO.success(CategoryResDTOList));
    }

    /**
     *
     * Post /categories
     * 카테고리 생성
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createCategory(@RequestBody @Valid CreateCategoryReqDTO categoryReqDTO) {
        Category category = new Category(null, categoryReqDTO.getName(), 1L);
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(createdCategory.getId()).build()));
    }

    /**
     *
     * PATCH /categories
     * 카테고리 수정
     */
    @PatchMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateCategory(@RequestBody UpdateCategoryReqDTO categoryReqDTO) {
        Category category = new Category(categoryReqDTO.getId(), categoryReqDTO.getName(), 1L);
        Category updatedCategory = categoryService.updateCategory(category);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(updatedCategory.getId()).build()));
    }

    /**
     *
     * DELETE /categories
     * 카테고리 삭제
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(categoryId).build()));
    }
}
