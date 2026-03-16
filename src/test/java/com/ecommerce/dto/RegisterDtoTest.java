package com.ecommerce.dto;

import com.ecommerce.entity.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterDtoTest {

    @Test
    void gettersAndSetters_shouldWork() {
        RegisterDto dto = new RegisterDto();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setPassword("password123");
        dto.setRole(Role.CUSTOMER);

        assertThat(dto.getName()).isEqualTo("John Doe");
        assertThat(dto.getEmail()).isEqualTo("john@example.com");
        assertThat(dto.getPassword()).isEqualTo("password123");
        assertThat(dto.getRole()).isEqualTo(Role.CUSTOMER);
    }

    @Test
    void role_shouldBeOptional() {
        RegisterDto dto = new RegisterDto();
        dto.setRole(null);
        assertThat(dto.getRole()).isNull();
    }
}
