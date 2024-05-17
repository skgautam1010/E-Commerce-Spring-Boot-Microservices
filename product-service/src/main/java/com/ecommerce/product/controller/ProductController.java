package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProducts(@Valid @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateProducts(@RequestBody ProductDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(productService.updateProduct(dto, id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProducts(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product is Deleted Successfully " + id);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductsById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam String sortingDir) {
        return ResponseEntity.ok(productService.getAllProducts(page, size, sortBy, sortingDir));
    }
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProducts(String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
       return ResponseEntity.ok(productService.searchProduct(keyword,page,size));
    }
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductDto>> filterProducts(@RequestParam(required = false) Long categoryId, @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.filterProducts(categoryId, minPrice, maxPrice, page, size));
    }
    @GetMapping("/advanceFiltering")
    public ResponseEntity<Page<ProductDto>> advanceFiltering(@RequestParam(required = false) String keyword, @RequestParam(required = false) Long categoryId, @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam String sortingDir ) {
        return ResponseEntity.ok(productService.advanceFilter(keyword, categoryId, minPrice, maxPrice, page, size, sortBy, sortingDir));
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<ProductDto> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(productService.uploadImage(id, file));
    }


}
