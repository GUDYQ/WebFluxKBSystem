package org.example.webfluxlearning.base.file;

import reactor.core.publisher.Mono;

import java.util.Set;

public interface FileHandler {
    Mono<FileProcessingContext> handle(FileProcessingContext context);
    Set<String> supportType();
}
