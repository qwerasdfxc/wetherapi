package com.example.weatherapi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value(value = "${jwt.secret}")
    private String secret;

    public String extractUsername(String authToken) {

        return getClaimsFromToken(authToken).getSubject();
    }

    public boolean validateToken(String authToken) {
        return getClaimsFromToken(authToken).getExpiration().before(new Date());
    }

    private Claims getClaimsFromToken(String authToken){
        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJwt(authToken)
                .getBody();
    }

}
