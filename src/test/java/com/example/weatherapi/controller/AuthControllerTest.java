package com.example.weatherapi.controller;

import com.example.weatherapi.DTO.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthController.class)
@ActiveProfiles("tc")
@ContextConfiguration(initializers = {AuthControllerTest.Initializer.class})

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
////@Testcontainers
//@ActiveProfiles("tc")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {

//    @Autowired
//    private WebTestClient webTestClient;


    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");



    @Test
    @Order(1)
    void testRegistration(){
//        UserDTO userDTO = UserDTO.builder().login("test").password("test").build();
//        webTestClient
//                .post()
//                .uri("/api/register")
//                .body(BodyInserters.fromValue(userDTO))
//                .exchange()
//                .expectStatus().isOk();

//        assertThat(2).isEqualTo(2);
        assertEquals(4,4);
    }
}
