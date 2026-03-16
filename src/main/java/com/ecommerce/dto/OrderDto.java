package com.ecommerce.dto;

import com.ecommerce.entity.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private Boolean paymentStatus;
    private OrderStatus orderStatus;
    private List<OrderItemDto> items;
}
