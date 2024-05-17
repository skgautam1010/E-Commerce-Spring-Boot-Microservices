package com.ecommerce.product.controller;

import com.ecommerce.product.dto.CategoryDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.updateCategory(dto, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category is deleted successfully " + id);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }
}
