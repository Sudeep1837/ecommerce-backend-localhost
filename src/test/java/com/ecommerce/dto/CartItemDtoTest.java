package com.ecommerce.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemDtoTest {

    @Test
    void gettersAndSetters_shouldWork() {
        CartItemDto dto = new CartItemDto();
        dto.setId(1L);
        dto.setProductId(5L);
        dto.setProductName("Item");
        dto.setPrice(29.99);
        dto.setQuantity(3);
        dto.setSubTotal(89.97);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getProductId()).isEqualTo(5L);
        assertThat(dto.getProductName()).isEqualTo("Item");
        assertThat(dto.getPrice()).isEqualTo(29.99);
        assertThat(dto.getQuantity()).isEqualTo(3);
        assertThat(dto.getSubTotal()).isEqualTo(89.97);
    }
}
