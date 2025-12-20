package org.example.webfluxlearning.service;

import org.example.webfluxlearning.service.component.strategy.StrategySelectionChain;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class TestService {

    @Autowired
    private StrategySelectionChain strategySelectionChain;

    @Cacheable("test")
    public Mono<String> hello() {
        return Mono.just("hello");
    }
}