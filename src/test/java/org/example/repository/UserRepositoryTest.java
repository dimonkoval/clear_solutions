package org.example.repository;

import org.example.exception.DatabaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Container
    static MySQLContainer<?> database;
//    @Container
//    static MySQLContainer<?> database = new MySQLContainer<>("mysql:latest")
//            .withDatabaseName("clear_solutions")
//            .withPassword("root")
//            .withUsername("070282");
    static {
        try {
            database = new MySQLContainer<>("mysql:8")
                    .withDatabaseName("clear_solutions")
                    .withUsername("root")
                    .withPassword("carService");
            database.start();
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to start container", e);
        }
    }

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        if (database != null) {
            try {
                registry.add("spring.datasource.username", database::getUsername);
                registry.add("spring.datasource.password", database::getPassword);
                registry.add("spring.datasource.url", database::getJdbcUrl);
            } catch (DatabaseException e) {
                throw new DatabaseException("Failed to set registration properties", e);
            }
        }
    }

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByBirthDateBetween() {
    }
}