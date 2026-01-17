package org.example.webfluxlearning.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<String> uploadFile(FilePart filePart);
}
