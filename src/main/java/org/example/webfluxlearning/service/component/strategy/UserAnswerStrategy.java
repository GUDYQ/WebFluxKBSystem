package org.example.webfluxlearning.service.component.strategy;

import org.example.webfluxlearning.entity.VO.AIModelApiRequest;
import org.example.webfluxlearning.service.component.client.ClientManager;
import org.example.webfluxlearning.service.component.client.LLMClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserAnswerStrategy implements ModelSelectionStrategy{

    @Autowired
    private ClientManager clientManager;

    @Override
    public Boolean support(String modelName) {
        return null;
    }

    @Override
    public Mono<LLMClient> execute(AIModelApiRequest request) {
        return clientManager.getClient(request.getModelName());
    }

    @Override
    public Long getOrder() {
        return 0L;
    }
}
