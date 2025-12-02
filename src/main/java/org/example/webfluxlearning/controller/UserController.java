package org.example.webfluxlearning.controller;

import org.example.webfluxlearning.base.response.ResponseBuilder;
import org.example.webfluxlearning.base.response.ResponseVO;
import org.example.webfluxlearning.entity.PO.User;
import org.example.webfluxlearning.entity.VO.UserToken;
import org.example.webfluxlearning.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;


    @PostMapping("/login")
    public Mono<ResponseVO<UserToken>> login() {
        return userService.loginUser("test@gmail.com", "213312312").map(ResponseBuilder::success);
    }

    @PostMapping("/register")
    public Mono<User> register() {
        return userService.registerUser("test", "213312312", "test@gmail.com");
    }

    @PostMapping("/{userId}/refreshToken")
    public Mono<String> generateAccessToken(@PathVariable String userId, @RequestParam String refreshToken) {
        return userService.generateAccessToken(userId, refreshToken);
    }

    @PostMapping("/logout")
    public String logout() {
        return "logout success";
    }

}