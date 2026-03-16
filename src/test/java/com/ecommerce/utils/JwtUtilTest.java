package com.ecommerce.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private static final long EXPIRATION = 86400000L; // 24 hours

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET, EXPIRATION);
        userDetails = User.builder()
                .username("john@example.com")
                .password("encoded")
                .roles("CUSTOMER")
                .build();
    }

    @Test
    void generateToken_shouldReturnNonEmptyToken() {
        String token = jwtUtil.generateToken(userDetails);

        assertThat(token).isNotBlank();
    }

    @Test
    void extractUsername_shouldReturnUsernameFromToken() {
        String token = jwtUtil.generateToken(userDetails);

        String username = jwtUtil.extractUsername(token);

        assertThat(username).isEqualTo("john@example.com");
    }

    @Test
    void extractExpiration_shouldReturnFutureDate() {
        String token = jwtUtil.generateToken(userDetails);

        java.util.Date expiration = jwtUtil.extractExpiration(token);

        assertThat(expiration).isAfter(new java.util.Date());
    }

    @Test
    void validateToken_shouldReturnTrue_whenTokenValidAndUserMatches() {
        String token = jwtUtil.generateToken(userDetails);

        Boolean valid = jwtUtil.validateToken(token, userDetails);

        assertThat(valid).isTrue();
    }

    @Test
    void validateToken_shouldReturnFalse_whenUsernameMismatch() {
        String token = jwtUtil.generateToken(userDetails);
        UserDetails otherUser = User.builder()
                .username("other@example.com")
                .password("pwd")
                .roles("CUSTOMER")
                .build();

        Boolean valid = jwtUtil.validateToken(token, otherUser);

        assertThat(valid).isFalse();
    }

    @Test
    void extractClaim_shouldExtractCustomClaim() {
        String token = jwtUtil.generateToken(userDetails);

        String subject = jwtUtil.extractClaim(token, claims -> claims.getSubject());

        assertThat(subject).isEqualTo("john@example.com");
    }
}
