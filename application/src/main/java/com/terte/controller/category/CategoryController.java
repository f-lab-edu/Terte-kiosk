package com.terte.controller.category;

import com.terte.dto.common.ApiResDTO;
import com.terte.dto.common.CommonIdResDTO;
import com.terte.dto.menu.CategoryResDTO;
import com.terte.dto.menu.CreateCategoryReqDTO;
import com.terte.dto.menu.UpdateCategoryReqDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/categories")
public class CategoryController {
    /**
     * GET /categories
     *  카테고리 조회
     */
    @GetMapping
    public ResponseEntity<ApiResDTO<List<CategoryResDTO>>> getCategoryList() {
        //return categoryService.getCategoryList();
        CategoryResDTO category1 = CategoryResDTO.builder()
                .id(1L)
                .name("COFFEE")
                .build();
        CategoryResDTO category2 = CategoryResDTO.builder()
                .id(2L)
                .name("BEVERAGE")
                .build();
        return ResponseEntity.ok(ApiResDTO.success(List.of(category1, category2)));
    }

    /**
     *
     * Post /categories
     * 카테고리 생성
     */
    @PostMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> createCategory(@RequestBody @Valid CreateCategoryReqDTO categoryReqDTO) {
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(3L).build()));
    }

    /**
     *
     * PATCH /categories
     * 카테고리 수정
     */
    @PatchMapping
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> updateCategory(@RequestBody UpdateCategoryReqDTO categoryReqDTO) {
        if(categoryReqDTO.getId() != 1L) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(categoryReqDTO.getId()).build()));
    }

    /**
     *
     * DELETE /categories
     * 카테고리 삭제
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResDTO<CommonIdResDTO>> deleteCategory(@PathVariable Long categoryId) {
        //categoryService.deleteCategory(categoryId);
        if(categoryId != 1L) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResDTO.success(CommonIdResDTO.builder().id(categoryId).build()));
    }
}
