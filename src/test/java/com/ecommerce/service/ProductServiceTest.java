package com.ecommerce.service;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void addProduct_shouldPersistAndReturnDto() {
        ProductDto dto = new ProductDto();
        dto.setName("P1");
        Product entity = Product.builder().id(1L).name("P1").build();

        when(modelMapper.map(dto, Product.class)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(entity);
        when(modelMapper.map(entity, ProductDto.class)).thenReturn(dto);

        ProductDto result = productService.addProduct(dto);

        assertThat(result).isEqualTo(dto);
        verify(productRepository).save(entity);
    }

    @Test
    void updateProduct_shouldUpdate_whenExists() {
        ProductDto dto = new ProductDto();
        dto.setName("Updated");
        dto.setDescription("desc");
        dto.setPrice(10.0);
        dto.setStock(5);
        dto.setCategory("cat");
        dto.setImageUrl("url");
        dto.setRating(4.5);

        Product existing = Product.builder().id(1L).name("Old").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(existing);
        when(modelMapper.map(existing, ProductDto.class)).thenReturn(dto);

        ProductDto result = productService.updateProduct(1L, dto);

        assertThat(result.getName()).isEqualTo("Updated");
        verify(productRepository).save(existing);
    }

    @Test
    void updateProduct_shouldThrow_whenNotFound() {
        ProductDto dto = new ProductDto();
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(1L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void deleteProduct_shouldDelete_whenExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_shouldThrow_whenNotExists() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> productService.deleteProduct(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void getProductById_shouldReturnDto_whenExists() {
        Product entity = Product.builder().id(1L).name("P1").build();
        ProductDto dto = new ProductDto();

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, ProductDto.class)).thenReturn(dto);

        ProductDto result = productService.getProductById(1L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getAllProducts_shouldFilterByCategoryOrName() {
        Page<Product> page = new PageImpl<>(java.util.List.of(Product.builder().id(1L).build()));
        when(productRepository.findByCategoryContainingIgnoreCase(eq("cat"), any(Pageable.class)))
                .thenReturn(page);
        when(productRepository.findByNameContainingIgnoreCase(eq("name"), any(Pageable.class)))
                .thenReturn(page);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(any(Product.class), eq(ProductDto.class))).thenReturn(new ProductDto());

        productService.getAllProducts(0, 10, "id", "asc", "cat", null);
        verify(productRepository).findByCategoryContainingIgnoreCase(eq("cat"), any(Pageable.class));

        productService.getAllProducts(0, 10, "id", "asc", null, "name");
        verify(productRepository).findByNameContainingIgnoreCase(eq("name"), any(Pageable.class));

        productService.getAllProducts(0, 10, "id", "asc", null, null);
        verify(productRepository).findAll(any(Pageable.class));
    }
}

