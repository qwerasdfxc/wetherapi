package com.example.weatherapi.config;

import com.example.weatherapi.model.Station;
import com.example.weatherapi.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Configuration
@EnableRedisRepositories
@EnableCaching
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.timeout}")
    private int timeout;

    @Bean
    @Primary
    ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(){
        return new LettuceConnectionFactory();
    }


    @Bean
    public ReactiveRedisTemplate<String, Station> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Station> valueSerializer =
                new Jackson2JsonRedisSerializer<>(Station.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Station> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, Station> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, Weather> reactiveRedisTemplateWeather(
            ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Weather> valueSerializer =
                new Jackson2JsonRedisSerializer<>(Weather.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Weather> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, Weather> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }



//  
//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//        jedisConnectionFactory.setPort(port);
//        jedisConnectionFactory.setHostName(host);
//        return jedisConnectionFactory;
//    }

//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        //log.info("redis host: "+redisProperties.getHost()+", port: "+redisProperties.getPort()+", password: "+redisProperties.getPassword());
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
//        redisStandaloneConfiguration.setDatabase(0);
////        redisStandaloneConfiguration.set
////        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
//        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration
//                .builder()
//                .connectTimeout(Duration.ofSeconds(timeout))
//                .readTimeout(Duration.ofSeconds(timeout));
////        if (redisProperties.isSsl())
////            builder.useSsl();
////         Final JedisClientConfiguration
//        JedisClientConfiguration clientConfig = builder.usePooling().poolConfig() .build();//.usePooling().build();
//        //TODO: Later: Add configurations for connection pool sizing.
//        //Create JedisConnectionFactory
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, clientConfig);
//        jedisConnectionFactory.setUsePool(false);
//        return jedisConnectionFactory;
//    }

//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
//        redisStandaloneConfiguration.setPassword(RedisPassword.of(""));
//        return new JedisConnectionFactory(redisStandaloneConfiguration);
//    }


//     JedisPoolConfig jedisPoolConfig(){
//
//    }
//    @Bean
//    RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory());
//        return redisTemplate;
//    }

//    @Bean
//    ReactiveRedisOperations<String, Station> redisOperations(ReactiveRedisConnectionFactory factory) {
//        Jackson2JsonRedisSerializer<Station> serializer = new Jackson2JsonRedisSerializer<>(Station.class);
//
//        RedisSerializationContext.RedisSerializationContextBuilder<String, Station> builder =
//                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
//
//        RedisSerializationContext<String, Station> context = builder.value(serializer).build();
//
//        return new ReactiveRedisTemplate<>(factory, context);
//    }



//
//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName("localhost");
//        redisStandaloneConfiguration.setPort(6379);
//        redisStandaloneConfiguration.setDatabase(0);
//        redisStandaloneConfiguration.setPassword(RedisPassword.of(""));
//
//        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
//        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60));// 60s connection timeout
//
//        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
//                jedisClientConfiguration.build());
//
//        return jedisConFactory;
//    }
}
