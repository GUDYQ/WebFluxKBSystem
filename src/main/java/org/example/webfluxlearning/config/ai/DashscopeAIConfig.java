package org.example.webfluxlearning.config.ai;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "spring.ai.dashscope")
public class DashscopeAIConfig {
    String apiKey;
    String workspaceId;
}
