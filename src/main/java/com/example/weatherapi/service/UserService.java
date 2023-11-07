package com.example.weatherapi.service;

import com.example.weatherapi.DTO.UserDTO;
import com.example.weatherapi.exception.AuthException;
import com.example.weatherapi.mapper.UserMapper;
import com.example.weatherapi.model.User;
import com.example.weatherapi.repository.db.UserRepository;
import com.example.weatherapi.repository.redis.UserRedisRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
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

    private final UserRedisRepository userRedisRepository;

//    private final ReactiveValueOperations<String,>

    public UserService(UserRepository userRepository, UserMapper userMapper, UserRedisRepository userRedisRepository) throws NoSuchAlgorithmException {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRedisRepository = userRedisRepository;
        keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(new SecureRandom());

    }

    public Mono<UserDTO> findUserByLogin(String username) {
        return userRepository.findFirstByLogin(username).flatMap(x -> Mono.just(userMapper.map(x)));
    }

    public Mono<Boolean> existsByKey(String key) {
        return userRedisRepository.existsByKey(key).flatMap(x -> {
            if (x)
                return Mono.just(true);
            else {
                return userRepository.existsByKey(key).flatMap(y -> {
                    if(y){
                        saveInRedisByKey(key).subscribe();
                        return Mono.just(true);
                    }
                    else
                        return Mono.just(false);
                });
            }
        });
//        return userRepository.existsByKey(key);
    }

//    private Mono<Boolean> saveInRedisIfExistsInDB(String key){
//        return userRepository.existsByKey(key).map(x -> {
//            if(x){
//                return saveInRedisByKey(key);
//            }
//            else
//                return false;
//        });
//    }

    private Mono<Boolean> saveInRedisByKey(String key){
        return userRepository.findByKey(key).flatMap(userRedisRepository::saveUser);
    }

    public Mono<UserDTO> saveIfNotExists(UserDTO userDTO) {
        return userRepository.existsByLogin(userDTO.getLogin()).flatMap(x -> {
            if (!x) {
                userDTO.setKey(generateKey());
                return userRepository.save(userMapper.map(userDTO)).map(userMapper::map);
            } else
                return Mono.error(new AuthException("User already exists", "400"));
        });
    }



    @SneakyThrows
    public String generateKey() {
        Key key = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(cipher.ENCRYPT_MODE, key);

        String msg = new String("test");
        byte[] bytes = cipher.doFinal(msg.getBytes());
//        return new String(bytes, StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
