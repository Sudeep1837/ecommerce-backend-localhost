package com.ecommerce.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemDtoTest {

    @Test
    void gettersAndSetters_shouldWork() {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(1L);
        dto.setProductId(5L);
        dto.setProductName("Widget");
        dto.setQuantity(2);
        dto.setPrice(19.99);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getProductId()).isEqualTo(5L);
        assertThat(dto.getProductName()).isEqualTo("Widget");
        assertThat(dto.getQuantity()).isEqualTo(2);
        assertThat(dto.getPrice()).isEqualTo(19.99);
    }
}
