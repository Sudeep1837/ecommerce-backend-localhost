package com.ecommerce.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void success_shouldBuildResponseWithTrueAndData() {
        String message = "Done";
        String data = "result";

        ApiResponse<String> response = ApiResponse.success(message, data);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void error_shouldBuildResponseWithFalseAndMessage() {
        String message = "Something went wrong";

        ApiResponse<Void> response = ApiResponse.error(message);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void builder_shouldSetAllFields() {
        ApiResponse<Integer> response = ApiResponse.<Integer>builder()
                .success(true)
                .message("OK")
                .data(42)
                .timestamp(LocalDateTime.now())
                .build();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("OK");
        assertThat(response.getData()).isEqualTo(42);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void noArgsConstructorAndSetters_shouldWork() {
        ApiResponse<String> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("err");
        response.setData(null);
        response.setTimestamp(LocalDateTime.now());

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("err");
    }
}
