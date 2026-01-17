package org.example.webfluxlearning.service.component.file.common;

import com.google.common.io.CharStreams;
import org.apache.tika.Tika;
import org.example.webfluxlearning.base.file.FileHandler;
import org.example.webfluxlearning.base.file.FileProcessingContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Set;

@Component
@Order(5)
public class FileAnalyzer implements FileHandler {

    private final Tika tika = new Tika();;

    private FileProcessingContext extractFileContext(InputStream inputStream, FileProcessingContext context) throws IOException {
        try(Reader reader = tika.parse(inputStream)) {
            String text = CharStreams.toString(reader);
            return context.setExtractedText(text);
        }
    }

    @Override
    public Mono<FileProcessingContext> handle(FileProcessingContext context) {
        return DataBufferUtils.join(context.getFilePart().content())
                .map(DataBuffer::asInputStream)
                .flatMap(inputStream ->
                        Mono.fromCallable(() ->
                                        extractFileContext(inputStream, context)).subscribeOn(Schedulers.boundedElastic())
                        .onErrorResume(throwable -> Mono.just(context.setStatus(FileProcessingContext.ProcessingStatus.FAIL)
                                .setExtractedText("Extraction failed: " + throwable.getMessage()))));
    }

    @Override
    public Set<String> supportType() {
        return Set.of("*");
    }
}
