package com.ecommerce.service;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendOrderConfirmation_shouldSendEmail() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .password("encoded")
                .role(Role.CUSTOMER)
                .build();

        Order order = Order.builder()
                .id(1L)
                .totalAmount(100.0)
                .items(Collections.emptyList())
                .build();

        emailService.sendOrderConfirmation(user, order);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();
        assertThat(message.getTo()).contains("john@example.com");
        assertThat(message.getSubject()).contains("Order Confirmation");
    }
}

