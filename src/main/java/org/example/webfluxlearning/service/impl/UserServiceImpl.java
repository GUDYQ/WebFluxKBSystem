package org.example.webfluxlearning.service.impl;

import org.example.webfluxlearning.base.exception.login.PasswordErrorException;
import org.example.webfluxlearning.base.exception.login.UserLoginException;
import org.example.webfluxlearning.base.request.UserTokenManager;
import org.example.webfluxlearning.base.security.anthentication.CustomAuthenticationManager;
import org.example.webfluxlearning.dao.repository.UserRepository;
import org.example.webfluxlearning.entity.PO.User;
import org.example.webfluxlearning.entity.VO.UserToken;
import org.example.webfluxlearning.service.UserService;
import org.example.webfluxlearning.utils.PBKDF2Util;
import org.example.webfluxlearning.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenManager userTokenManager;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PBKDF2Util pbkdf2Util;

    @Autowired
    private CustomAuthenticationManager authenticationManager;

    @Override
    public Mono<UserToken> loginUser(String userEmail, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password))
                .onErrorMap(e -> new PasswordErrorException("Authentication failed"))
                .map(Authentication::getPrincipal)
                .cast(UserDetails.class)
                .switchIfEmpty(Mono.error(new PasswordErrorException("Authentication failed")))
                .map(UserDetails::getUsername)
                .flatMap(email -> userRepository.findFirstByEmail(userEmail))
                .flatMap(user -> Mono.fromCallable(() -> {
                            String refreshToken = userTokenManager.generateRefreshToken(user.getUuid());
                            String accessToken = userTokenManager.generateTokens(user.getUuid(), user.getUsername(),
                                    user.getEmail());
                            System.out.println(accessToken);
                            return new UserToken(refreshToken, accessToken);
                        }
                ).subscribeOn(Schedulers.boundedElastic())
                        .flatMap(userToken ->
                        redisUtil.setExpire(user.getUuid(), userToken.getAccessToken(), (long) 24 * 60 * 60)
                                .onErrorMap(e -> new PasswordErrorException("Authentication failed"))
                                .thenReturn(userToken)
                ));
    }

    public Mono<String> generateAccessToken(String userId, String refreshToken) throws UserLoginException {
        return redisUtil.get(userId).cast(String.class)
                .filter(s -> s != null && s.equals(refreshToken))
                .switchIfEmpty(Mono.error(new UserLoginException("Invalid refresh token")))
                .then(userRepository.findById(userId))
                .flatMap(user -> Mono.fromCallable(() ->
                        userTokenManager.generateTokens(
                                user.getUuid(),
                                user.getUsername(),
                                user.getEmail())
                ).subscribeOn(Schedulers.boundedElastic()));
    }

    @Override
    public Mono<User> registerUser(String username, String password, String email) {
        if (username == null || username.isBlank()) {
            return Mono.error(new IllegalArgumentException("Username is required"));
        }
        if (password == null || password.length() < 8) {
            return Mono.error(new IllegalArgumentException("Password too weak"));
        }
        return Mono.fromCallable(() -> pbkdf2Util.hashPassword(password))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(hashedPassword -> {
                    User user = new User();
                    user.setPasswordHashHex(hashedPassword);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setCreatedTime(LocalDateTime.now());
                    user.setUuid(UUID.randomUUID().toString());
                    return userRepository.save(user);
                });
    }

    @Override
    public Mono<Boolean> logoutUser() {
        return null;
    }
}
