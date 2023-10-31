package com.example.weatherapi.service;

import com.example.weatherapi.DTO.UserDTO;
import com.example.weatherapi.exception.AuthException;
import com.example.weatherapi.mapper.UserMapper;
import com.example.weatherapi.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
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

//    private final ReactiveValueOperations<String,>

    public UserService(UserRepository userRepository, UserMapper userMapper) throws NoSuchAlgorithmException {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(new SecureRandom());

    }

    public Mono<UserDTO> findUserByLogin(String username) {
        return userRepository.findFirstByLogin(username).flatMap(x -> Mono.just(userMapper.map(x)));
    }

    public Mono<Boolean> existsByKey(String key){
        return userRepository.existsByKey(key);
    }


    public Mono<UserDTO> saveIfNotExists(UserDTO userDTO){
        return userRepository.existsByLogin(userDTO.getLogin()).flatMap(x -> {
            if(!x){
                userDTO.setKey(generateKey());
                return userRepository.save(userMapper.map(userDTO)).map(userMapper::map);
            }
            else
                return Mono.error(new AuthException("User already exists", "400"));
        });
    }



    @SneakyThrows
    public String generateKey(){
        Key key = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(cipher.ENCRYPT_MODE, key);

        String msg = new String("test");
        byte[] bytes = cipher.doFinal(msg.getBytes());
//        return new String(bytes, StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
