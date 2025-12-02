package org.example.webfluxlearning.service;

import org.example.webfluxlearning.dao.repository.UserRepository;
import org.example.webfluxlearning.entity.VO.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserAuthenticationService implements ReactiveUserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String userEmail) {
        return userRepository.findFirstByEmail(userEmail).switchIfEmpty(Mono.error(new UsernameNotFoundException(userEmail)))
                .flatMap(user -> Mono.just(new UserAuthentication(user, List.of("ROLE_USER"))));
    }
}
