package com.example.weatherapi.service;

import com.example.weatherapi.DTO.UserDTO;
import com.example.weatherapi.exception.AuthException;
import com.example.weatherapi.mapper.UserMapper;
import com.example.weatherapi.model.User;
import com.example.weatherapi.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private final KeyGenerator keyGenerator;
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) throws NoSuchAlgorithmException {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(new SecureRandom());

    }

//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        return userRepository.findFirstByUsername(username).cast(UserDetails.class);
//    }

    public Mono<UserDTO> findUserByUsername(String username) {
        return userRepository.findFirstByUsername(username).flatMap(x -> Mono.just(userMapper.map(x)));
    }

    public Mono<Boolean> existsByKey(String key){
        return userRepository.existsByKey(key);
    }

//    public Mono<User> save(User user){
//        user.setKey(generateKey());
//        return userRepository.save(user);
//    }

    public Mono<UserDTO> saveIfNotExists(User user){
        return userRepository.existsByUsername(user.getUsername()).flatMap(x -> {
            if(!x)
                return userRepository.save(user).flatMap(y -> Mono.just(userMapper.map(y)));
            else
                return Mono.error(new AuthException("User already exists", "400"));
        });
    }
}
