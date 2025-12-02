package org.example.webfluxlearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class WebFluxLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxLearningApplication.class, args);
    }

}
