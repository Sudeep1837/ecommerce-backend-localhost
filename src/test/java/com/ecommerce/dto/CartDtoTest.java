package com.ecommerce.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartDtoTest {

    @Test
    void gettersAndSetters_shouldWork() {
        CartDto dto = new CartDto();
        dto.setId(1L);
        dto.setUserId(10L);
        dto.setTotalPrice(150.0);
        dto.setItems(List.of(new CartItemDto()));

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUserId()).isEqualTo(10L);
        assertThat(dto.getTotalPrice()).isEqualTo(150.0);
        assertThat(dto.getItems()).hasSize(1);
    }
}
