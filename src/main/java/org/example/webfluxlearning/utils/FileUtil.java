package org.example.webfluxlearning.utils;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class FileUtil {
    Mono<String> generateFileName(String cacheName) {
        return Mono.just(UUID.randomUUID().toString());
    }
    Mono<Path> saveFile(FilePart file) {
        return Mono.fromCallable(() -> {
                    String safeName = UUID.randomUUID() + "." + file.filename();
                    return Files.createTempFile("kb-upload-", safeName);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(tempPath ->
                        DataBufferUtils.write(file.content(), tempPath)
                                .thenReturn(tempPath) // 携带路径继续
                );
    }
}
