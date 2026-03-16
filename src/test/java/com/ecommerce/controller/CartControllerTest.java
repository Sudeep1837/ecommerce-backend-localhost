package com.ecommerce.controller;

import com.ecommerce.dto.CartDto;
import com.ecommerce.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        when(authentication.getName()).thenReturn("john@example.com");
    }

    @Test
    void getCart_shouldReturnOk() throws Exception {
        when(cartService.getCart(any())).thenReturn(new CartDto());

        mockMvc.perform(get("/api/cart").principal(authentication))
                .andExpect(status().isOk());

        verify(cartService).getCart("john@example.com");
    }

    @Test
    void addToCart_shouldReturnOk() throws Exception {
        when(cartService.addToCart(any(), eq(1L), any())).thenReturn(new CartDto());

        mockMvc.perform(post("/api/cart/add/1")
                        .principal(authentication)
                        .param("quantity", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService).addToCart("john@example.com", 1L, 2);
    }

    @Test
    void updateCartItem_shouldReturnOk() throws Exception {
        when(cartService.updateCartItem(any(), eq(1L), eq(3))).thenReturn(new CartDto());

        mockMvc.perform(put("/api/cart/update/1")
                        .principal(authentication)
                        .param("quantity", "3"))
                .andExpect(status().isOk());

        verify(cartService).updateCartItem("john@example.com", 1L, 3);
    }

    @Test
    void removeFromCart_shouldReturnOk() throws Exception {
        when(cartService.removeFromCart(any(), eq(1L))).thenReturn(new CartDto());

        mockMvc.perform(delete("/api/cart/remove/1").principal(authentication))
                .andExpect(status().isOk());

        verify(cartService).removeFromCart("john@example.com", 1L);
    }
}

