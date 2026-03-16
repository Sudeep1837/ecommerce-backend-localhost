package com.ecommerce.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDtoTest {

    @Test
    void gettersAndSetters_shouldWork() {
        ProductDto dto = new ProductDto();
        dto.setId(1L);
        dto.setName("Phone");
        dto.setDescription("Smartphone");
        dto.setPrice(299.0);
        dto.setStock(10);
        dto.setCategory("Electronics");
        dto.setImageUrl("http://img.url");
        dto.setRating(4.5);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Phone");
        assertThat(dto.getDescription()).isEqualTo("Smartphone");
        assertThat(dto.getPrice()).isEqualTo(299.0);
        assertThat(dto.getStock()).isEqualTo(10);
        assertThat(dto.getCategory()).isEqualTo("Electronics");
        assertThat(dto.getImageUrl()).isEqualTo("http://img.url");
        assertThat(dto.getRating()).isEqualTo(4.5);
    }
}
