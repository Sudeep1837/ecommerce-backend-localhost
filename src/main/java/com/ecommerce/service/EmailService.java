package com.ecommerce.service;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOrderConfirmation(User user, Order order) {
        // If mail is not fully configured, at least log the content
        String subject = "Order Confirmation - Order #" + order.getId();
        StringBuilder text = new StringBuilder();
        text.append("Hi ").append(user.getName()).append(",\n\n")
                .append("Thank you for your purchase! Your order has been placed successfully.\n\n")
                .append("Order ID: ").append(order.getId()).append("\n")
                .append("Total Amount: ").append(order.getTotalAmount()).append("\n")
                .append("Status: ").append(order.getOrderStatus()).append("\n\n")
                .append("Items:\n");

        for (OrderItem item : order.getItems()) {
            text.append("- ")
                    .append(item.getProduct().getName())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append(" @ ")
                    .append(item.getPrice())
                    .append("\n");
        }

        text.append("\nRegards,\nE-Commerce Team");

        log.info("Sending order confirmation email to {}: {}", user.getEmail(), text);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject(subject);
            message.setText(text.toString());
            mailSender.send(message);
        } catch (Exception ex) {
            // Log and move on; email failure should not break checkout
            log.error("Failed to send order confirmation email", ex);
        }
    }
}

