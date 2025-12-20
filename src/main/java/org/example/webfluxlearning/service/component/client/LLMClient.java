package org.example.webfluxlearning.service.component.client;

import reactor.core.publisher.Flux;

public interface LLMClient {
    String modelName();
    Flux<String> streamChat(String message);
    void refreshModel();
}
