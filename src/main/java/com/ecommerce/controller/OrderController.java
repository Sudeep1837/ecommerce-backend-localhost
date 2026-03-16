package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderDto>> checkout(Authentication authentication) {
        log.info("Checkout requested for current user");
        return ResponseEntity.ok(ApiResponse.success("Checkout process completed", orderService.checkout(authentication.getName())));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getUserOrders(Authentication authentication) {
        log.debug("Fetching orders for current user");
        return ResponseEntity.ok(ApiResponse.success("Orders fetched", orderService.getUserOrders(authentication.getName())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable Long id) {
        log.debug("Fetching order id={}", id);
        return ResponseEntity.ok(ApiResponse.success("Order fetched", orderService.getOrderById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("Admin updating order id={} status={}", id, status);
        return ResponseEntity.ok(ApiResponse.success("Order status updated", orderService.updateOrderStatus(id, status)));
    }
}
