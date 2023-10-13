package com.example.weatherapi.security;

import com.example.weatherapi.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

public class JwtHandler {

    @Value("${jwt.secret}")
    private String secret;

    public JwtHandler(String secret) {
        this.secret = secret;
    }

    public Mono<VerificationResult> check(String token) {
        return Mono.just(verify(token))
                .onErrorResume(e -> Mono.error(new AuthException("Unauthorized", "Unauthorized")));
    }


    private VerificationResult verify(String token) {
        Claims claims = getClaimsFormToken(token);
        Date expiration = claims.getExpiration();

        if (expiration.before(new Date()))
            throw new AuthException("Token Expired", "EXPIRED");

        return new VerificationResult(claims, token);
    }

    private Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }


    public static class VerificationResult {
        public Claims claims;
        public String token;

        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }
}
