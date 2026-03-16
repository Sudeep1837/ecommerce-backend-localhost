package com.ecommerce.service;

import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.LoginDto;
import com.ecommerce.dto.RegisterDto;
import com.ecommerce.dto.UserDto;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldCreateUserAndReturnToken_whenEmailNotExists() {
        RegisterDto dto = new RegisterDto();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPassword("password");

        User user = User.builder()
                .id(1L)
                .name(dto.getName())
                .email(dto.getEmail())
                .password("encoded")
                .role(Role.CUSTOMER)
                .build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("CUSTOMER")
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("token");
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(new UserDto());

        AuthResponse response = authService.register(dto);

        assertThat(response.getToken()).isEqualTo("token");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        RegisterDto dto = new RegisterDto();
        dto.setEmail("john@example.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email is already registered");
    }

    @Test
    void login_shouldAuthenticateAndReturnToken_whenCredentialsValid() {
        LoginDto dto = new LoginDto();
        dto.setEmail("john@example.com");
        dto.setPassword("password");

        User user = User.builder()
                .id(1L)
                .email(dto.getEmail())
                .password("encoded")
                .role(Role.CUSTOMER)
                .build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("CUSTOMER")
                .build();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("token");
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto());

        AuthResponse response = authService.login(dto);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertThat(response.getToken()).isEqualTo("token");
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        LoginDto dto = new LoginDto();
        dto.setEmail("notfound@example.com");
        dto.setPassword("password");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}

