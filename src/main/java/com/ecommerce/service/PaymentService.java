package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentService {

    private final Random random;

    public PaymentService() {
        this(new Random());
    }

    @Autowired(required = false)
    public PaymentService(Random random) {
        this.random = random != null ? random : new Random();
    }

    // Simulate payment processing. Returns true 90% of the time.
    public boolean processPayment(Double amount) {
        return random.nextInt(100) > 10;
    }
}
