package com.ecommerce.controller;

import com.ecommerce.dto.*;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        when(authentication.getName()).thenReturn("john@example.com");
    }

    @Test
    void register_shouldReturnOk() throws Exception {
        when(authService.register(any(RegisterDto.class))).thenReturn(new AuthResponse());

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"email\":\"john@example.com\",\"password\":\"pass123\"}"))
                .andExpect(status().isOk());

        verify(authService).register(any(RegisterDto.class));
    }

    @Test
    void login_shouldReturnOk() throws Exception {
        when(authService.login(any(LoginDto.class))).thenReturn(new AuthResponse());

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@example.com\",\"password\":\"pass123\"}"))
                .andExpect(status().isOk());

        verify(authService).login(any(LoginDto.class));
    }

    @Test
    void getProfile_shouldReturnOk() throws Exception {
        when(userService.getUserProfile(any())).thenReturn(new UserDto());

        mockMvc.perform(get("/api/users/profile").principal(authentication))
                .andExpect(status().isOk());

        verify(userService).getUserProfile("john@example.com");
    }

    @Test
    void changePassword_shouldReturnOk() throws Exception {
        doNothing().when(userService).changePassword(any(), any(ChangePasswordDto.class));

        mockMvc.perform(put("/api/users/change-password")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentPassword\":\"old\",\"newPassword\":\"newPass123\"}"))
                .andExpect(status().isOk());

        verify(userService).changePassword(eq("john@example.com"), any(ChangePasswordDto.class));
    }

    @Test
    void getAllUsers_shouldReturnOk() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());

        verify(userService).getAllUsers();
    }
}

