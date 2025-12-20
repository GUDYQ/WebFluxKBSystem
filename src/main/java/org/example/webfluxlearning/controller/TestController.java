package org.example.webfluxlearning.controller;

import org.example.webfluxlearning.base.response.ResponseBuilder;
import org.example.webfluxlearning.base.response.ResponseVO;
import org.example.webfluxlearning.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/hello")
    public Mono<ResponseVO<String>> hello() {
        return testService.hello().map(ResponseBuilder::success);
    }
}
