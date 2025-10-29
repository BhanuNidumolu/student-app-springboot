package learn.studentmanagment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest // <-- This was already here
@Testcontainers   // <-- ADD THIS
class StudentManagmentApplicationTests {

    // --- ADD ALL OF THIS ---
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", ()->"validate");
        registry.add("spring.flyway.enabled", ()->"true");
    }
    // --- END OF ADDED CODE ---

    @Test
    void contextLoads() {
        // This test will now run successfully
    }
}