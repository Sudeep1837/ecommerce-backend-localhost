package com.ecommerce.service;

import com.ecommerce.dto.CartDto;
import com.ecommerce.dto.CartItemDto;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartDto getCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = getOrCreateCart(user);
        log.debug("Fetched cart for userId={}", user.getId());
        return mapToDto(cart);
    }

    public CartDto addToCart(String email, Long productId, Integer quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStock() < quantity) {
            log.warn("Insufficient stock for productId={} requestedQty={} available={}", productId, quantity, product.getStock());
            throw new BadRequestException("Not enough stock available");
        }

        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(newItem);
        }

        updateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        log.info("Added productId={} quantity={} to cartId={}", productId, quantity, savedCart.getId());
        return mapToDto(savedCart);
    }

    public CartDto updateCartItem(String email, Long productId, Integer quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = getOrCreateCart(user);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
            log.info("Removed productId={} from cartId={} due to non-positive quantity", productId, cart.getId());
        } else {
            if (item.getProduct().getStock() < quantity) {
                log.warn("Insufficient stock while updating cart for productId={} requestedQty={} available={}",
                        productId, quantity, item.getProduct().getStock());
                throw new BadRequestException("Not enough stock available");
            }
            item.setQuantity(quantity);
        }

        updateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        log.info("Updated cartId={} for productId={} newQuantity={}", savedCart.getId(), productId, quantity);
        return mapToDto(savedCart);
    }

    public CartDto removeFromCart(String email, Long productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = getOrCreateCart(user);

        boolean removed = cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        if (removed) {
            log.info("Removed productId={} from cartId={}", productId, cart.getId());
        }

        updateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        return mapToDto(savedCart);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });
    }

    private void updateCartTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(CartItem::getSubTotal)
                .sum();
        cart.setTotalPrice(total);
    }

    private CartDto mapToDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setItems(cart.getItems().stream().map(item -> {
            CartItemDto itemDto = new CartItemDto();
            itemDto.setId(item.getId());
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setPrice(item.getProduct().getPrice());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setSubTotal(item.getSubTotal());
            return itemDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}
