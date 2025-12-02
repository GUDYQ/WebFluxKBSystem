package org.example.webfluxlearning.config.secretkey;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "spring.config.jwt")
public class JWTConfig {
    @NotNull
    private Integer expirationMinutes;
    @NotNull
    private Long expirationKeyDays;
}
