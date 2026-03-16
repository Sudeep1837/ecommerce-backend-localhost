package com.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private Long id;
    private Long userId;
    private Double totalPrice;
    private List<CartItemDto> items;
}
