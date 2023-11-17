package com.example.weatherapi.config;

import com.example.weatherapi.service.UserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
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

//    @Override
//    public Mono filter(ServerRequest request, HandlerFunction next) {
//        if (!bucket.tryConsume(1)) {
////            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(429));
//            return ServerResponse.status(HttpStatusCode.valueOf(429)).build();
////            return Mono.empty();
//
//        }
//        if(request.exchange().getRequest().getPath().toString().contains("stations")){
//            return userService.existsByKey(request.exchange().getRequest().getHeaders().getFirst("Authorization")).map(result -> {
//                if (result)
//                    return next.handle(request);
////                    return chain.filter(exchange);
//                else
//                {
////                    request.exchange().getResponse().setStatusCode(HttpStatusCode.valueOf(401));
////                    return Mono.error(new AuthenticationException("key isn't valid"));
////                    return ServerResponse.status(HttpStatusCode.valueOf(401)).bodyValue("Key isn't valid");
//                    return Mono.error(new AuthenticationException("2345"));
//                }
//            });
////                    .then(chain.filter(exchange));
//        }
//        else
//            return next.handle(request);
//
//    }

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
//                    exchange.getRequest().
//                    return Mono.error(new AuthenticationException("key isn't valid"));
                    return Mono.error(new AuthenticationException("key isn't valid"));
                }
            }).then();
        }
        else
            return chain.filter(exchange);
    }
}
