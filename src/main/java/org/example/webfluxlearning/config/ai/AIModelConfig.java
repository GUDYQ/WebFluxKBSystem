package org.example.webfluxlearning.config.ai;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class AIModelConfig {
    @Autowired
    private final DashscopeAIConfig dashscopeAIConfig;
}
