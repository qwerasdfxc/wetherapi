package com.example.weatherapi.security;

import com.example.weatherapi.exception.AuthException;
import com.example.weatherapi.model.User;
import com.example.weatherapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

//    private final JwtUtil jwtUtil;
//
//    public AuthenticationManager(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
    private final UserRepository userRepository;


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal)  authentication.getPrincipal();
        return userRepository.findById(principal.getId())
//                .filter(User::isEnabled)
//                .switchIfEmpty(Mono.error(new AuthException("User is disabled", "DISABLED")))
                .map(user -> authentication);


//
//        String authToken = authentication.getCredentials().toString();
//
//        String username;
//        try {
//
//         username = jwtUtil.extractUsername(authToken);
//        }catch (Exception e){
//            System.out.println(e);
//            username = null;
//        }
//
//        if (username != null && jwtUtil.validateToken(authToken)) {
//            SimpleGrantedAuthority authorities = new SimpleGrantedAuthority("user");
//
//             return Mono.just(new UsernamePasswordAuthenticationToken(
//                    username,
//                    null,
//                    null
//            ));
//        }
//        else {
//            return Mono.empty();
//        }
    }

}
