package org.example.webfluxlearning.service.component.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Qualifier("qwen")
public class QwenClient implements LLMClient{

    private final ChatClient chatClient;

    public QwenClient(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public String modelName() {
        return "qwen";
    }

    @Override
    public Flux<String> streamChat(String message) {

        return this.chatClient.prompt(message)
                .stream()
                .content();
    }

    @Override
    public void refreshModel() {

    }
}
