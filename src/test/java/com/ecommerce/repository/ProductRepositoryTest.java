package com.ecommerce.repository;

import com.ecommerce.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByCategoryContainingIgnoreCase_shouldReturnMatchingProducts() {
        Product p1 = Product.builder().name("Phone").category("Electronics").price(99.0).stock(10).build();
        Product p2 = Product.builder().name("Laptop").category("Electronics").price(499.0).stock(5).build();
        Product p3 = Product.builder().name("Shirt").category("Clothing").price(29.0).stock(20).build();
        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(p3);
        entityManager.flush();

        Pageable page = PageRequest.of(0, 10);
        var result = productRepository.findByCategoryContainingIgnoreCase("electronics", page);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting(Product::getCategory).containsOnly("Electronics");
    }

    @Test
    void findByNameContainingIgnoreCase_shouldReturnMatchingProducts() {
        Product p1 = Product.builder().name("Smartphone").category("Tech").price(199.0).stock(10).build();
        Product p2 = Product.builder().name("Smart Watch").category("Tech").price(99.0).stock(5).build();
        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.flush();

        Pageable page = PageRequest.of(0, 10);
        var result = productRepository.findByNameContainingIgnoreCase("smart", page);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting(Product::getName).containsExactlyInAnyOrder("Smartphone", "Smart Watch");
    }

    @Test
    void save_shouldPersistProduct() {
        Product product = Product.builder()
                .name("Test Product")
                .category("Test")
                .price(10.0)
                .stock(5)
                .build();

        Product saved = productRepository.save(product);

        assertThat(saved.getId()).isNotNull();
        assertThat(productRepository.findById(saved.getId())).isPresent();
    }
}
