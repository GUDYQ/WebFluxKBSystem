package org.example.webfluxlearning.service.component.client;

import org.example.webfluxlearning.config.ai.AIModelConfig;
import org.example.webfluxlearning.base.exception.ai.AiClientCreateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Component
public class ClientManager {

    @Autowired
    private Map<String, LLMClient> clientMap;

    @Autowired
    private AIModelConfig aiModelConfig;

    public Mono<LLMClient> getClient(String modelName) {
        return Mono.justOrEmpty(clientMap.get(modelName))
                .switchIfEmpty(Mono.error(new AiClientCreateException("Model not supported: " + modelName)));
    }
}
