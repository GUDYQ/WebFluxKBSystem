package org.example.webfluxlearning.service;

import org.example.webfluxlearning.entity.PO.User;
import org.example.webfluxlearning.entity.VO.UserToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserToken> loginUser(String userEmail, String password);
    Mono<User> registerUser(String username, String password, String email);
    Mono<Boolean> logoutUser();
}
