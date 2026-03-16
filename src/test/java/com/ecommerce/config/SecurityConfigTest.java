package com.ecommerce.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import jakarta.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises SecurityConfig branches: public vs protected endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicEndpoints_shouldBePermitAll() throws Exception {
        // PermitAll: must not return 401 (allowed without auth)
        ResultMatcher notUnauthorized = result ->
                assertThat(result.getResponse().getStatus()).isNotEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        mockMvc.perform(get("/api/users/register")).andExpect(notUnauthorized);
        mockMvc.perform(post("/api/users/login").contentType("application/json").content("{}"))
                .andExpect(notUnauthorized);
    }

    @Test
    void protectedEndpoint_shouldReturnUnauthorizedWithoutToken() throws Exception {
        mockMvc.perform(get("/api/cart")).andExpect(status().isUnauthorized());
    }

    @Test
    void swaggerUi_shouldBePermitAll() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().is2xxSuccessful());
    }
}
