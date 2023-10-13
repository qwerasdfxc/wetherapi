//package com.example.weatherapi.config;
//
//import com.example.weatherapi.model.User;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.util.*;
//
//@Component
//public class JwtUtil {
//
//    @Value(value = "${jwt.secret}")
//    private String secret;
//
//    @Value(value = "${jwt.expiration}")
//    private String  expiration;
//
//
//    private final KeyGenerator keyGenerator;
//
//    public JwtUtil() throws NoSuchAlgorithmException {
//        keyGenerator = KeyGenerator.getInstance("DES");
//        keyGenerator.init(new SecureRandom());
//    }
//
//    public String extractUsername(String authToken) {
//
//        return getClaimsFromToken(authToken).getSubject();
//    }
//
//    public boolean validateToken(String authToken) {
//        return getClaimsFromToken(authToken).getExpiration().before(new Date());
//    }
//
//    private Claims getClaimsFromToken(String authToken){
//        return Jwts.parser()
//                .setSigningKey(secret)
//                .build()
//                .parseClaimsJwt(authToken)
//                .getBody();
//    }
//
//    @SneakyThrows
//    public String generateKey(){
//        Key key = keyGenerator.generateKey();
//        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//        cipher.init(cipher.ENCRYPT_MODE, key);
//
//        String msg = new String("test");
//        byte[] bytes = cipher.doFinal(msg.getBytes());
////        return new String(bytes, StandardCharsets.UTF_8);
//        return Base64.getEncoder().encodeToString(bytes);
//    }
//
//    public String generateJWT(User user) {
//        Date date = new Date(new Date().getTime() + Long.parseLong(expiration) * 1000);
//        Map map = new HashMap();
//        return Jwts.builder()
//                .setClaims(map)
//                .setSubject(user.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(date)
//                .signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();
//
//    }
//}
