package com.ecommerce.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChangePasswordDtoTest {

    @Test
    void gettersAndSetters_shouldWork() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setCurrentPassword("oldPass");
        dto.setNewPassword("newPass123");

        assertThat(dto.getCurrentPassword()).isEqualTo("oldPass");
        assertThat(dto.getNewPassword()).isEqualTo("newPass123");
    }
}
