package com.ecommerce.controller;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        when(authentication.getName()).thenReturn("john@example.com");
    }

    @Test
    void checkout_shouldReturnOk() throws Exception {
        when(orderService.checkout(any())).thenReturn(new OrderDto());

        mockMvc.perform(post("/api/orders/checkout").principal(authentication))
                .andExpect(status().isOk());

        verify(orderService).checkout("john@example.com");
    }

    @Test
    void getUserOrders_shouldReturnOk() throws Exception {
        when(orderService.getUserOrders(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/orders").principal(authentication))
                .andExpect(status().isOk());

        verify(orderService).getUserOrders("john@example.com");
    }

    @Test
    void getOrderById_shouldReturnOk() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(new OrderDto());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk());

        verify(orderService).getOrderById(1L);
    }

    @Test
    void updateOrderStatus_shouldReturnOk() throws Exception {
        when(orderService.updateOrderStatus(eq(1L), eq("PLACED"))).thenReturn(new OrderDto());

        mockMvc.perform(put("/api/orders/1/status").param("status", "PLACED"))
                .andExpect(status().isOk());

        verify(orderService).updateOrderStatus(1L, "PLACED");
    }
}

