package com.ecommerce.service;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.entity.*;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("encoded")
                .role(Role.CUSTOMER)
                .build();

        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(100.0)
                .stock(10)
                .category("Test")
                .build();

        CartItem cartItem = CartItem.builder()
                .id(1L)
                .product(product)
                .quantity(2)
                .build();

        cart = Cart.builder()
                .id(1L)
                .user(user)
                .totalPrice(200.0)
                .items(new ArrayList<>(Collections.singletonList(cartItem)))
                .build();
        cartItem.setCart(cart);
    }

    @Test
    void checkout_shouldCreateOrderAndReduceStock_whenPaymentSuccessful() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(paymentService.processPayment(200.0)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            order.setOrderDate(LocalDateTime.now());
            return order;
        });

        OrderDto result = orderService.checkout(user.getEmail());

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTotalAmount()).isEqualTo(200.0);
        assertThat(result.getItems()).hasSize(1);
        assertThat(product.getStock()).isEqualTo(8);

        verify(productRepository, times(1)).save(product);
        verify(cartRepository, times(1)).save(cart);
        verify(emailService, times(1)).sendOrderConfirmation(any(User.class), any(Order.class));
    }

    @Test
    void checkout_shouldThrow_whenCartEmpty() {
        cart.setItems(Collections.emptyList());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> orderService.checkout(user.getEmail()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cart is empty");
    }

    @Test
    void checkout_shouldSetCancelledStatus_whenPaymentFails() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(paymentService.processPayment(200.0)).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            order.setOrderDate(LocalDateTime.now());
            return order;
        });

        OrderDto result = orderService.checkout(user.getEmail());

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
        verify(productRepository, never()).save(any());
        verify(emailService, never()).sendOrderConfirmation(any(), any());
    }

    @Test
    void getUserOrders_shouldReturnOrderDtos() {
        Order order = Order.builder()
                .id(1L)
                .user(user)
                .totalAmount(100.0)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PLACED)
                .items(new ArrayList<>())
                .build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(orderRepository.findByUserId(user.getId())).thenReturn(List.of(order));

        List<OrderDto> result = orderService.getUserOrders(user.getEmail());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getTotalAmount()).isEqualTo(100.0);
    }

    @Test
    void getUserOrders_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getUserOrders("missing@example.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void getOrderById_shouldReturnOrderDto() {
        Order order = Order.builder()
                .id(1L)
                .user(user)
                .totalAmount(100.0)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PLACED)
                .items(new ArrayList<>())
                .build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDto result = orderService.getOrderById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTotalAmount()).isEqualTo(100.0);
    }

    @Test
    void getOrderById_shouldThrow_whenOrderNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    void updateOrderStatus_shouldUpdateAndReturnDto() {
        Order order = Order.builder()
                .id(1L)
                .user(user)
                .totalAmount(100.0)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PLACED)
                .items(new ArrayList<>())
                .build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderDto result = orderService.updateOrderStatus(1L, "SHIPPED");

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.SHIPPED);
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderStatus_shouldThrow_whenInvalidStatus() {
        Order order = Order.builder()
                .id(1L)
                .user(user)
                .orderStatus(OrderStatus.PLACED)
                .items(new ArrayList<>())
                .build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateOrderStatus(1L, "INVALID_STATUS"))
                .isInstanceOf(com.ecommerce.exception.BadRequestException.class)
                .hasMessageContaining("Invalid order status");
    }
}


