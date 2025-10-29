package learn.studentmanagment.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import learn.studentmanagment.Dto.StudentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.function.Supplier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// This annotation tells Spring Boot to load the full application context
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc // This auto-configures MockMvc for sending HTTP requests
@Testcontainers
@Transactional
class StudentControllerTest {

    // This creates a MySQL Docker container for our tests
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26");

    @Autowired
    private MockMvc mockMvc; // The tool for sending fake HTTP requests

    @Autowired
    private ObjectMapper objectMapper; // For converting Java objects to JSON

    // This method dynamically sets the database properties *before* Spring starts.
    // It points our app to the test database we just created.
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto",()->"validate");
        registry.add("spring.flyway.enabled", ()->"true");
    }

    @Test
    @DisplayName("GET /api/students - Fails for Unauthenticated User")
    void getAllStudents_FailsUnauthenticated() throws Exception {
        // --- ACT & ASSERT ---
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isUnauthorized()); // Expect 401
    }

    @Test
    @DisplayName("GET /api/students - Succeeds for Authenticated STUDENT")
    // @WithMockUser simulates a logged-in user with this role
    @WithMockUser(roles = "STUDENT")
    void getAllStudents_SucceedsWithStudentRole() throws Exception {
        // --- ACT & ASSERT ---
        // Note: The CommandLineRunner in your main app DOES NOT run in tests.
        // So the database is empty.
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray()) // Expect a JSON array
                .andExpect(jsonPath("$").isEmpty()); // Expect it to be empty
    }

    @Test
    @DisplayName("POST /api/students - Fails for STUDENT Role")
    @WithMockUser(roles = "STUDENT")
    void createStudent_FailsWithStudentRole() throws Exception {
        // --- ARRANGE ---
        StudentRequestDto requestDto = new StudentRequestDto();
        // ... (set fields) ...

        // --- ACT & ASSERT ---
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden()); // Expect 403 Forbidden
    }

    @Test
    @DisplayName("POST /api/students - Succeeds for ADMIN Role")
    @WithMockUser(roles = "ADMIN")
    void createStudent_SucceedsWithAdminRole() throws Exception {
        // --- ARRANGE ---
        StudentRequestDto requestDto = new StudentRequestDto();
        requestDto.setFirstName("Integration");
        requestDto.setLastName("Test");
        requestDto.setEmail("test@example.com");
        requestDto.setUsername("integrationtest");
        requestDto.setPassword("password12345");
        requestDto.setAge(30);

        // --- ACT & ASSERT ---
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated()) // Expect 201 Created
                .andExpect(jsonPath("$.id").exists()) // Expect ID to be generated
                .andExpect(jsonPath("$.firstName").value("Integration"))
                .andExpect(jsonPath("$.username").value("integrationtest"));
    }
}