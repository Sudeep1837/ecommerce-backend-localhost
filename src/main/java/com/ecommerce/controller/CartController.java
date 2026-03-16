package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.CartDto;
import com.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart(Authentication authentication) {
        log.debug("Fetching cart for current user");
        return ResponseEntity.ok(ApiResponse.success("Cart fetched successfully", cartService.getCart(authentication.getName())));
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> addToCart(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            Authentication authentication) {
        log.info("Adding to cart productId={} quantity={}", productId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Item added to cart", cartService.addToCart(authentication.getName(), productId, quantity)));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> updateCartItem(
            @PathVariable Long productId,
            @RequestParam Integer quantity,
            Authentication authentication) {
        log.info("Updating cart item productId={} quantity={}", productId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Cart updated", cartService.updateCartItem(authentication.getName(), productId, quantity)));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> removeFromCart(
            @PathVariable Long productId,
            Authentication authentication) {
        log.info("Removing productId={} from cart", productId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", cartService.removeFromCart(authentication.getName(), productId)));
    }
}
