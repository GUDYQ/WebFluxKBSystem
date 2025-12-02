package org.example.webfluxlearning.service.component.strategy;


import org.example.webfluxlearning.entity.VO.AIModelApiRequest;
import org.example.webfluxlearning.service.component.client.LLMClient;
import reactor.core.publisher.Mono;

public interface ModelSelectionStrategy {
    Boolean support(String modelName);
    Mono<LLMClient> execute(AIModelApiRequest request);
    Long getOrder();
}
