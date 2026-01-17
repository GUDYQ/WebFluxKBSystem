package org.example.webfluxlearning.service.impl;

import org.example.webfluxlearning.service.FileService;
import org.example.webfluxlearning.service.component.file.FileHandlePipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileHandlePipeline handlePipeline;

    @Override
    public Mono<String> uploadFile(FilePart filePart) {
        return null;
    }
}
