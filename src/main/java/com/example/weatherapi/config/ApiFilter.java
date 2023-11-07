package com.example.weatherapi.config;

import com.example.weatherapi.service.UserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.time.Duration;

@Configuration
public class ApiFilter implements WebFilter {

    @Value("${bandwidthPerMinute}")
    private Integer bandwidthPerMinute;

    private final Bucket bucket;

    private final UserService userService;

    public ApiFilter(UserService userService) {
        this.userService = userService;
        Bandwidth bandwidth = Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(bandwidth)
                .build();


    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!bucket.tryConsume(1)) {
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(429));
            return Mono.empty();

        }
        if(exchange.getRequest().getPath().toString().contains("stations")){
            return userService.existsByKey(exchange.getRequest().getHeaders().getFirst("Authorization")).map(result -> {
                if (result)
                    return chain.filter(exchange);
                else
                {
                    exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
                    return Mono.empty();
                }
            }).then(chain.filter(exchange));
        }
        else
            return chain.filter(exchange);
    }
}
