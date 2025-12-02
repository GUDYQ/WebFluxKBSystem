package org.example.webfluxlearning.service.impl;

import org.example.webfluxlearning.base.response.ResponseVO;
import org.example.webfluxlearning.entity.VO.AIModelApiRequest;
import org.example.webfluxlearning.service.AIService;
import org.example.webfluxlearning.service.component.strategy.StrategySelectionChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private StrategySelectionChain strategySelectionChain;

    @Override
    public Mono<String> chat(AIModelApiRequest request) {
        return strategySelectionChain.askModel(request);
    }
}
