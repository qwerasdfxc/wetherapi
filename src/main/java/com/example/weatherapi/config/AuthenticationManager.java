package com.example.weatherapi.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    public AuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        String username;
        try {

         username = jwtUtil.extractUsername(authToken);
        }catch (Exception e){
            System.out.println(e);
            username = null;
        }

        if (username != null && jwtUtil.validateToken(authToken)) {
            SimpleGrantedAuthority authorities = new SimpleGrantedAuthority("user");

             return Mono.just(new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    null
            ));
        }
        else {
            return Mono.empty();
        }
    }

}
