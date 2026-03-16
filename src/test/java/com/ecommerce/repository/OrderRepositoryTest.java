package com.ecommerce.repository;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByUserId_shouldReturnOrdersForUser() {
        User user = User.builder().name("John").email("john@example.com").password("enc").role(Role.CUSTOMER).build();
        entityManager.persist(user);
        entityManager.flush();

        Order order1 = Order.builder()
                .user(user)
                .totalAmount(100.0)
                .orderDate(LocalDateTime.now())
                .paymentStatus(true)
                .orderStatus(OrderStatus.PLACED)
                .build();
        Order order2 = Order.builder()
                .user(user)
                .totalAmount(50.0)
                .orderDate(LocalDateTime.now())
                .paymentStatus(true)
                .orderStatus(OrderStatus.SHIPPED)
                .build();
        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.flush();

        List<Order> found = orderRepository.findByUserId(user.getId());

        assertThat(found).hasSize(2);
        assertThat(found).extracting(Order::getTotalAmount).containsExactlyInAnyOrder(100.0, 50.0);
    }

    @Test
    void findByUserId_shouldReturnEmptyList_whenNoOrders() {
        User user = User.builder().name("Jane").email("jane@example.com").password("enc").role(Role.CUSTOMER).build();
        entityManager.persist(user);
        entityManager.flush();

        List<Order> found = orderRepository.findByUserId(user.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void save_shouldPersistOrder() {
        User user = User.builder().name("New").email("new@example.com").password("enc").role(Role.CUSTOMER).build();
        entityManager.persist(user);
        entityManager.flush();

        Order order = Order.builder()
                .user(user)
                .totalAmount(99.0)
                .orderDate(LocalDateTime.now())
                .paymentStatus(true)
                .orderStatus(OrderStatus.PLACED)
                .build();
        Order saved = orderRepository.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(orderRepository.findById(saved.getId())).isPresent();
    }
}
