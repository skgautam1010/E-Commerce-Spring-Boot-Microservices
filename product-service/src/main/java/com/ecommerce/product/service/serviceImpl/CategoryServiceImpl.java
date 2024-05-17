package com.ecommerce.product.service.serviceImpl;

import com.ecommerce.product.dto.CategoryDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.mapper.CategoryMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        Category category = categoryMapper.toEntity(dto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto dto, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Not Found"));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Not Found"));
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        return categoryRepository.findAll().stream().map(categoryMapper :: toDto).toList();
    }
}
