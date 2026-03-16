package com.ecommerce.service;

import com.ecommerce.dto.ChangePasswordDto;
import com.ecommerce.dto.UserDto;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserProfile_shouldReturnDto_whenUserExists() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .role(Role.CUSTOMER)
                .build();
        UserDto dto = new UserDto();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(dto);

        UserDto result = userService.getUserProfile(user.getEmail());

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getUserProfile_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserProfile("missing@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void changePassword_shouldUpdatePassword_whenCurrentPasswordMatches() {
        User user = User.builder()
                .id(1L)
                .email("john@example.com")
                .password("encoded")
                .role(Role.CUSTOMER)
                .build();

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("old");
        dto.setNewPassword("newPass");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(dto.getNewPassword())).thenReturn("encodedNew");

        userService.changePassword(user.getEmail(), dto);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_shouldThrow_whenCurrentPasswordIncorrect() {
        User user = User.builder()
                .id(1L)
                .email("john@example.com")
                .password("encoded")
                .role(Role.CUSTOMER)
                .build();

        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("wrong");
        dto.setNewPassword("newPass");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword(user.getEmail(), dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Current password is incorrect");
    }

    @Test
    void getAllUsers_shouldReturnMappedDtos() {
        User user1 = User.builder().id(1L).email("a@test.com").build();
        User user2 = User.builder().id(2L).email("b@test.com").build();
        UserDto dto1 = new UserDto();
        UserDto dto2 = new UserDto();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(modelMapper.map(user1, UserDto.class)).thenReturn(dto1);
        when(modelMapper.map(user2, UserDto.class)).thenReturn(dto2);

        List<UserDto> result = userService.getAllUsers();

        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    void deleteUser_shouldInvokeRepository() {
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}

