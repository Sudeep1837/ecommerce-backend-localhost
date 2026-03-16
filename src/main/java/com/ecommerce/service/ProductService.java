package com.ecommerce.service;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductDto addProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        product = productRepository.save(product);
        log.info("Added product with id={} name={}", product.getId(), product.getName());
        return modelMapper.map(product, ProductDto.class);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());
        existingProduct.setCategory(productDto.getCategory());
        existingProduct.setImageUrl(productDto.getImageUrl());
        existingProduct.setRating(productDto.getRating());

        existingProduct = productRepository.save(existingProduct);
        log.info("Updated product with id={}", existingProduct.getId());
        return modelMapper.map(existingProduct, ProductDto.class);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            log.warn("Attempt to delete non-existing product id={}", id);
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
        log.info("Deleted product with id={}", id);
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        log.debug("Fetched product with id={}", id);
        return modelMapper.map(product, ProductDto.class);
    }

    public Page<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir, String category, String name) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage;

        if (category != null && !category.isEmpty()) {
            productPage = productRepository.findByCategoryContainingIgnoreCase(category, pageable);
        } else if (name != null && !name.isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        log.debug("Fetched products page={}, size={}, category={}, name={}", page, size, category, name);

        return productPage.map(product -> modelMapper.map(product, ProductDto.class));
    }
}
