package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> addProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Admin adding new product name={}", productDto.getName());
        return new ResponseEntity<>(ApiResponse.success("Product added successfully", productService.addProduct(productDto)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        log.info("Admin updating product id={}", id);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", productService.updateProduct(id, productDto)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        log.info("Admin deleting product id={}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id) {
        log.debug("Fetching product id={}", id);
        return ResponseEntity.ok(ApiResponse.success("Product fetched", productService.getProductById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String sortDir,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name
    ) {
        log.debug("Fetching products list page={} size={} category={} name={}", page, size, category, name);
        return ResponseEntity.ok(ApiResponse.success("Products fetched", productService.getAllProducts(page, size, sortBy, sortDir, category, name)));
    }
}
