package com.ecommerce.dto;

import com.ecommerce.entity.OrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDtoTest {

    @Test
    void gettersAndSetters_shouldWork() {
        OrderDto dto = new OrderDto();
        dto.setId(1L);
        dto.setUserId(10L);
        dto.setTotalAmount(99.99);
        LocalDateTime now = LocalDateTime.now();
        dto.setOrderDate(now);
        dto.setPaymentStatus(true);
        dto.setOrderStatus(OrderStatus.PLACED);
        dto.setItems(List.of(new OrderItemDto()));

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUserId()).isEqualTo(10L);
        assertThat(dto.getTotalAmount()).isEqualTo(99.99);
        assertThat(dto.getOrderDate()).isEqualTo(now);
        assertThat(dto.getPaymentStatus()).isTrue();
        assertThat(dto.getOrderStatus()).isEqualTo(OrderStatus.PLACED);
        assertThat(dto.getItems()).hasSize(1);
    }
}
