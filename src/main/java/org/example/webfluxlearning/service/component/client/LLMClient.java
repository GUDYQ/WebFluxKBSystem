package org.example.webfluxlearning.service.component.client;

import reactor.core.publisher.Flux;

public interface LLMClient {
    String modelName();
    String chat(String message);
    Flux<String> streamChat(String message);
    void refreshModel();
}
