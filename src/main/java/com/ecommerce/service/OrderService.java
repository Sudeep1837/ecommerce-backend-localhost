package com.ecommerce.service;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.OrderItemDto;
import com.ecommerce.entity.*;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;
    private final EmailService emailService;

    public OrderDto checkout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            log.warn("Checkout attempt with empty cart for userId={}", user.getId());
            throw new BadRequestException("Cart is empty");
        }

        // Validate stock
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                log.warn("Insufficient stock during checkout for productId={} requestedQty={} available={}",
                        product.getId(), item.getQuantity(), product.getStock());
                throw new BadRequestException("Not enough stock for product: " + product.getName());
            }
        }

        Double totalAmount = cart.getTotalPrice();

        // Process payment
        boolean paymentSuccess = paymentService.processPayment(totalAmount);
        log.info("Payment processed for userId={} amount={} success={}", user.getId(), totalAmount, paymentSuccess);

        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .orderDate(LocalDateTime.now())
                .paymentStatus(paymentSuccess)
                .orderStatus(paymentSuccess ? OrderStatus.PLACED : OrderStatus.CANCELLED)
                .build();

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            return OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getProduct().getPrice())
                    .build();
        }).collect(Collectors.toList());

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created with id={} userId={} status={}", savedOrder.getId(), user.getId(), savedOrder.getOrderStatus());

        if (paymentSuccess) {
            // Deduct stock
            for (CartItem item : cart.getItems()) {
                Product product = item.getProduct();
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
            }
            log.info("Inventory updated for orderId={}", savedOrder.getId());

            // Clear cart
            cart.getItems().clear();
            cart.setTotalPrice(0.0);
            cartRepository.save(cart);
            log.info("Cart cleared for userId={} cartId={}", user.getId(), cart.getId());

            // Send confirmation email (best-effort)
            emailService.sendOrderConfirmation(user, savedOrder);
        }

        return mapToDto(savedOrder);
    }

    public List<OrderDto> getUserOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepository.findByUserId(user.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapToDto(order);
    }

    public OrderDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        
        try {
            order.setOrderStatus(OrderStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid order status");
        }
        
        Order savedOrder = orderRepository.save(order);
        return mapToDto(savedOrder);
    }

    private OrderDto mapToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setItems(order.getItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setId(item.getId());
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).collect(Collectors.toList()));
        return dto;
    }
}
