package com.ecommerce.repository;

import com.ecommerce.entity.Cart;
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
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByUserId_shouldReturnCart_whenExists() {
        User user = User.builder().name("John").email("john@example.com").password("enc").role(Role.CUSTOMER).build();
        entityManager.persist(user);
        entityManager.flush();

        Cart cart = Cart.builder().user(user).totalPrice(0.0).build();
        entityManager.persist(cart);
        entityManager.flush();

        Optional<Cart> found = cartRepository.findByUserId(user.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(found.get().getTotalPrice()).isEqualTo(0.0);
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenNoCart() {
        User user = User.builder().name("Jane").email("jane@example.com").password("enc").role(Role.CUSTOMER).build();
        entityManager.persist(user);
        entityManager.flush();

        Optional<Cart> found = cartRepository.findByUserId(user.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void save_shouldPersistCart() {
        User user = User.builder().name("New").email("new@example.com").password("enc").role(Role.CUSTOMER).build();
        entityManager.persist(user);
        entityManager.flush();

        Cart cart = Cart.builder().user(user).totalPrice(50.0).build();
        Cart saved = cartRepository.save(cart);

        assertThat(saved.getId()).isNotNull();
        assertThat(cartRepository.findByUserId(user.getId())).isPresent();
    }
}
