package com.ecommerce.service;

import com.ecommerce.dto.ChangePasswordDto;
import com.ecommerce.dto.UserDto;
import com.ecommerce.entity.User;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.debug("Fetched profile for user: {}", email);
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto updateUserProfile(String email, UserDto userDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(userDto.getName());
        // Email update might require re-authentication or verification, keeping it simple for now
        // user.setEmail(userDto.getEmail());

        User updatedUser = userRepository.save(user);
        log.info("Updated profile for user: {}", email);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    public void changePassword(String email, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            log.warn("Failed password change attempt due to incorrect current password for user: {}", email);
            throw new BadRequestException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", email);
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        log.debug("Fetched all users, count={}", users.size());
        return users;
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.debug("Fetched user by id={}", id);
        return modelMapper.map(user, UserDto.class);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("Deleted user with id={}", id);
    }
}

