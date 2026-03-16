package com.ecommerce.repository;

import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        User user = User.builder()
                .name("John")
                .email("john@example.com")
                .password("encoded")
                .role(Role.CUSTOMER)
                .build();
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findByEmail("john@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
        assertThat(found.get().getName()).isEqualTo("John");
        assertThat(found.get().getRole()).isEqualTo(Role.CUSTOMER);
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailNotExists() {
        Optional<User> found = userRepository.findByEmail("missing@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        User user = User.builder()
                .name("Jane")
                .email("jane@example.com")
                .password("encoded")
                .role(Role.ADMIN)
                .build();
        entityManager.persist(user);
        entityManager.flush();

        boolean exists = userRepository.existsByEmail("jane@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailNotExists() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    void save_shouldPersistUser() {
        User user = User.builder()
                .name("New User")
                .email("new@example.com")
                .password("secret")
                .role(Role.CUSTOMER)
                .build();

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(userRepository.findByEmail("new@example.com")).isPresent();
    }
}
