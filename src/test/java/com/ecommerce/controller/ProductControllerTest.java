package com.ecommerce.controller;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void addProduct_shouldReturnCreated() throws Exception {
        when(productService.addProduct(any(ProductDto.class))).thenReturn(new ProductDto());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"P1\",\"price\":10,\"stock\":5,\"category\":\"cat\"}"))
                .andExpect(status().isCreated());

        verify(productService).addProduct(any(ProductDto.class));
    }

    @Test
    void getAllProducts_shouldReturnOk() throws Exception {
        when(productService.getAllProducts(anyInt(), anyInt(), anyString(), anyString(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(new ProductDto()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());

        verify(productService).getAllProducts(0, 10, "id", "asc", null, null);
    }

    @Test
    void getProductById_shouldReturnOk() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setId(1L);
        when(productService.getProductById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk());

        verify(productService).getProductById(1L);
    }

    @Test
    void updateProduct_shouldReturnOk() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setName("Updated");
        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(dto);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated\",\"price\":20,\"stock\":10,\"category\":\"cat\"}"))
                .andExpect(status().isOk());

        verify(productService).updateProduct(eq(1L), any(ProductDto.class));
    }

    @Test
    void deleteProduct_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk());

        verify(productService).deleteProduct(1L);
    }
}

