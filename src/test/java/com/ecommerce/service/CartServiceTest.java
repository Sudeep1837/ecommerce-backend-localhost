package com.ecommerce.service;

import com.ecommerce.dto.CartDto;
import com.ecommerce.entity.*;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).email("john@example.com").role(Role.CUSTOMER).build();
        product = Product.builder().id(1L).name("P1").price(50.0).stock(10).build();
        cart = Cart.builder().id(1L).user(user).items(new ArrayList<>()).totalPrice(0.0).build();
    }

    @Test
    void getCart_shouldReturnDto_whenUserHasCart() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        CartDto result = cartService.getCart(user.getEmail());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getTotalPrice()).isEqualTo(0.0);
    }

    @Test
    void getCart_shouldCreateCartAndReturnDto_whenUserHasNoCart() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> {
            Cart c = inv.getArgument(0);
            c.setId(2L);
            return c;
        });

        CartDto result = cartService.getCart(user.getEmail());

        assertThat(result).isNotNull();
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void getCart_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCart("missing@example.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void addToCart_shouldAddNewItem_whenEnoughStock() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartDto result = cartService.addToCart(user.getEmail(), product.getId(), 2);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getTotalPrice()).isEqualTo(100.0);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addToCart_shouldThrow_whenNotEnoughStock() {
        product.setStock(1);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> cartService.addToCart(user.getEmail(), product.getId(), 2))
                .isInstanceOf(com.ecommerce.exception.BadRequestException.class)
                .hasMessageContaining("Not enough stock available");
    }

    @Test
    void updateCartItem_shouldRemoveItem_whenQuantityZero() {
        CartItem item = CartItem.builder().id(1L).cart(cart).product(product).quantity(2).build();
        cart.getItems().add(item);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartDto result = cartService.updateCartItem(user.getEmail(), product.getId(), 0);

        assertThat(result.getItems()).isEmpty();
    }

    @Test
    void updateCartItem_shouldThrow_whenProductNotInCart() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        assertThatThrownBy(() -> cartService.updateCartItem(user.getEmail(), product.getId(), 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found in cart");
    }

    @Test
    void removeFromCart_shouldRemoveItem() {
        CartItem item = CartItem.builder().id(1L).cart(cart).product(product).quantity(1).build();
        cart.getItems().add(item);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartDto result = cartService.removeFromCart(user.getEmail(), product.getId());

        assertThat(result.getItems()).isEmpty();
    }
}

