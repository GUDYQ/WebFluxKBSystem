package org.example.webfluxlearning.service.component.strategy;

import org.example.webfluxlearning.base.exception.ai.AiRequestException;
import org.example.webfluxlearning.entity.VO.AIModelApiRequest;
import org.example.webfluxlearning.service.component.client.LLMClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class StrategySelectionChain {
    private final List<ModelSelectionStrategy> chain;

    @Autowired
    public StrategySelectionChain(Map<String, ModelSelectionStrategy> strategies) {
        this.chain = strategies.values().stream().sorted(
                (a, b) -> a.getOrder().compareTo(b.getOrder())
        ).toList();
    }

    public Flux<String> askModel(AIModelApiRequest request) throws AiRequestException {
        return Flux.fromIterable(chain).filter(strategy -> strategy.support(request.getModelName()))
                .next().switchIfEmpty(Mono.error(new AiRequestException("Non Model Support")))
                .flatMap(strategy -> strategy.execute(request))
                .flatMapMany(client -> client.streamChat(request.getContent()));
    }
}
