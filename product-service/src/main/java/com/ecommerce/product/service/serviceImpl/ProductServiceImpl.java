package com.ecommerce.product.service.serviceImpl;

import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.exception.ResourceNotFoundException;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/products/";
    private final long MAX_SIZE = 2*1024*1024;

    @Override
    public ProductDto createProduct(ProductDto dto) {
        Category category = null;
        if(dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Category not found"));
            category.setId(dto.getCategoryId());
        }
        Product product = productMapper.toEntity(dto,category);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto dto, Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product is Not Found"));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountedPrice(dto.getDiscountedPrice());
        product.setQuantity(dto.getQuantity());
        product.setBrand(dto.getBrand());
        product.setImageUrl(dto.getImageUrl());
        if(dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Category not found"));

            product.setCategory(category);
        }
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto uploadImage(Long id, MultipartFile multipartFile) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        if(multipartFile.isEmpty()) {
            throw new RuntimeException("Image File is Empty");
        }

        if(multipartFile.getSize() > MAX_SIZE) {
            throw new RuntimeException("Image File exceeds max size of 2 mb");
        }
        List<String> allowedFileTypes = List.of("image/jpeg","image/png","image/jpg");
        if(!allowedFileTypes.contains(multipartFile.getContentType())) {
            throw new RuntimeException("Image file must be of either PNG, JPG or JPEG Type");
        }
        String originalFileName = multipartFile.getOriginalFilename();
        if(originalFileName == null || !originalFileName.contains(".")) {
            throw new RuntimeException("Invalid File Name");
        }
        List<String> allowedExt = List.of("png", "jpg", "jpeg");
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);
        if(!allowedExt.contains(extension)) {
            throw new RuntimeException("Invalid Image extension");
        }
        File folder = new File(UPLOAD_DIR);
        if(!folder.exists()) {
            folder.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + "." + extension;
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, multipartFile.getBytes());
        String imageUrl = "/products/images/" + fileName;
        product.setImageUrl(imageUrl);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public Page<ProductDto> getAllProducts(int page, int size, String sortBy, String sortingDir) {
        Sort sort = sortingDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper :: toDto);
    }

    @Override
    public Page<ProductDto> searchProduct(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
        return products.map(productMapper :: toDto);
    }

    @Override
    public Page<ProductDto> filterProducts(Long categoryId, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.advanceFilter(null, categoryId, minPrice, maxPrice, pageable);
        return  productPage.map(productMapper :: toDto);
    }

    @Override
    public Page<ProductDto> advanceFilter(String keyword, Long categoryId, Double minPrice, Double maxPrice, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.advanceFilter(keyword, categoryId, minPrice, maxPrice, pageable);
        return productPage.map(productMapper :: toDto);
    }
}
