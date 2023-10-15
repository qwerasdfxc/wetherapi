package com.example.weatherapi.security;

import com.example.weatherapi.exception.AuthException;
import com.example.weatherapi.model.User;
import com.example.weatherapi.repository.UserRepository;
import com.ongres.scram.common.bouncycastle.base64.Base64;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;

    @Value("${jwt.issuer}")
    private String issuer;


    private TokenDetails generateToken(User user) {
        Map<String, Object> claims = new HashMap<>(){{
            put("role", "user");
            put("username", user.getLogin());
        }};
        return generateToken(claims, user.getId().toString());
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        Long expirationInMills = expirationInSeconds * 1000L;
        return generateToken(new Date(new Date().getTime() + expirationInMills), claims, subject);
    }

    private TokenDetails generateToken(Date expiration, Map<String, Object> claims, String subject) {
        Date date = new Date();
        String token = Jwts
                .builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(date)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, Base64.encode(secret.getBytes()))
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(date)
                .expiresAt(expiration)
                .build();
    }

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userRepository.findFirstByLogin(username)
                .flatMap(user -> {
//                    if (!user.isEnabled()) {
//                        return Mono.error(new AuthException("Account disabled", "DISABLED"));
//                    }

                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new AuthException("Invalid password", "INVALID"));
                    }
                    return Mono.just(generateToken(user).toBuilder()
                            .userId(user.getId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new AuthException("Invalid useranamej", "INVALID")));
    }


}
