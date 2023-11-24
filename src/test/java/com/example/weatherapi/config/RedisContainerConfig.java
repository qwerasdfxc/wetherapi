//package com.example.weatherapi.config;
//
//import com.redis.testcontainers.RedisContainer;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.utility.DockerImageName;
//
//@Configuration
//public class RedisContainerConfig {
//    @Container
//    private static final RedisContainer REDIS_CONTAINER =
//            new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);
//
//    @DynamicPropertySource
//    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
//        registry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
//    }
//}
