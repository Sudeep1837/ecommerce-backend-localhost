package com.ecommerce.service;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest {

    @Test
    void processPayment_shouldReturnTrue_whenRandomAboveThreshold() {
        Random highRandom = new Random() {
            @Override
            public int nextInt(int bound) {
                return 50; // 50 > 10 -> true
            }
        };
        PaymentService service = new PaymentService(highRandom);

        boolean result = service.processPayment(100.0);

        assertThat(result).isTrue();
    }

    @Test
    void processPayment_shouldReturnFalse_whenRandomBelowOrEqualThreshold() {
        Random lowRandom = new Random() {
            @Override
            public int nextInt(int bound) {
                return 5; // 5 > 10 -> false
            }
        };
        PaymentService service = new PaymentService(lowRandom);

        boolean result = service.processPayment(50.0);

        assertThat(result).isFalse();
    }

    @Test
    void processPayment_shouldAcceptAmountAndReturnBoolean_whenDefaultConstructor() {
        PaymentService service = new PaymentService();

        boolean result = service.processPayment(99.99);

        assertThat(result).isInstanceOf(Boolean.class);
    }
}
