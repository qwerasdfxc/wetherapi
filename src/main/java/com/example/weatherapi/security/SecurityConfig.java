package com.example.weatherapi.config;

import com.example.weatherapi.security.AuthenticationManager;
import com.example.weatherapi.security.BearerTokenServerAuthenticationConverter;
import com.example.weatherapi.security.JwtHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) {
        return http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                //TODO delete get-api-key
                .pathMatchers("/api/register", "/api/stations", "/api/stations/*", "/api/save_stations").permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> {
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatusCode.valueOf(401)));
                })
                .accessDeniedHandler((swe, e) -> {
                            return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatusCode.valueOf(403)));
                        })
                .and()
                .addFilterAt(bearerAuthenticationWebFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }



    //TODO ask Eugene why should I use pathMatcher here, why not simple path matchers
    private AuthenticationWebFilter bearerAuthenticationWebFilter(AuthenticationManager authenticationManager){
        AuthenticationWebFilter bearerAuthenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationWebFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
//        TODO dont work(like in video)
//        bearerAuthenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatcher.pattermatcher);

        bearerAuthenticationWebFilter.setRequiresAuthenticationMatcher(new PathPatternParserServerWebExchangeMatcher("/api/get-api-key"));
        return bearerAuthenticationWebFilter;
    }



//    private final AuthenticationManager authenticationManager;
//    private final SecurityContextRepository securityContextRepository;

//    public SecurityConfig(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
//        this.authenticationManager = authenticationManager;
//        this.securityContextRepository = securityContextRepository;
//    }
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .csrf().disable()
//                .httpBasic().disable()
//                .formLogin().disable()
//                .authenticationManager(authenticationManager)
//                .securityContextRepository(securityContextRepository)
//                .authorizeExchange()
//                .pathMatchers("/api/register", "/api/station*", "/api/get-api-key").permitAll()
//                .anyExchange().authenticated()
//                .and()
//                .addFilterAfter()
//                .and()
//                .build();
//    }


//    @Bean
//    public MapReactiveUserDetailsService userDetailsService() {
//        UserDetails userDetails = User
//                .withUsername("User")
//                .password(passwordEncoder().encode("1111"))
//                .build();
//        return new MapReactiveUserDetailsService(userDetails);
//    }



//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
