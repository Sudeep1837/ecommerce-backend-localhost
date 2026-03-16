package com.ecommerce.dto;

import com.ecommerce.entity.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    @Test
    void gettersAndSetters_shouldWork() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setRole(Role.CUSTOMER);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("John");
        assertThat(dto.getEmail()).isEqualTo("john@example.com");
        assertThat(dto.getRole()).isEqualTo(Role.CUSTOMER);
    }

    @Test
    void allArgsConstructor_shouldWork() {
        UserDto dto = new UserDto(2L, "Jane", "jane@example.com", Role.ADMIN);

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getName()).isEqualTo("Jane");
        assertThat(dto.getEmail()).isEqualTo("jane@example.com");
        assertThat(dto.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void equalsAndHashCode_shouldWork() {
        UserDto dto1 = new UserDto(1L, "John", "john@example.com", Role.CUSTOMER);
        UserDto dto2 = new UserDto(1L, "John", "john@example.com", Role.CUSTOMER);
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }
}
