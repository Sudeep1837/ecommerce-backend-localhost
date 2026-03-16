package com.ecommerce.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthResponseTest {

    @Test
    void builder_shouldSetAllFields() {
        UserDto userDto = new UserDto(1L, "John", "john@example.com", null);
        AuthResponse response = AuthResponse.builder()
                .token("jwt-token-123")
                .user(userDto)
                .build();

        assertThat(response.getToken()).isEqualTo("jwt-token-123");
        assertThat(response.getUser()).isEqualTo(userDto);
    }

    @Test
    void gettersAndSetters_shouldWork() {
        AuthResponse response = new AuthResponse();
        response.setToken("abc");
        response.setUser(new UserDto());

        assertThat(response.getToken()).isEqualTo("abc");
        assertThat(response.getUser()).isNotNull();
    }

    @Test
    void allArgsConstructor_shouldWork() {
        AuthResponse response = new AuthResponse("token", new UserDto());

        assertThat(response.getToken()).isEqualTo("token");
        assertThat(response.getUser()).isNotNull();
    }
}
