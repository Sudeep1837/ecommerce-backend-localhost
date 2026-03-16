package com.ecommerce.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginDtoTest {

    @Test
    void gettersAndSetters_shouldWork() {
        LoginDto dto = new LoginDto();
        dto.setEmail("user@example.com");
        dto.setPassword("secret123");

        assertThat(dto.getEmail()).isEqualTo("user@example.com");
        assertThat(dto.getPassword()).isEqualTo("secret123");
    }
}
