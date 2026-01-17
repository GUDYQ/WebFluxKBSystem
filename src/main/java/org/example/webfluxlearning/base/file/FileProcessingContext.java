package org.example.webfluxlearning.base.file;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.codec.multipart.FilePart;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class FileProcessingContext {
    private FilePart filePart;
    private String documentId;
    private Path tempPath;
    private String extractedText;
    private ProcessingStatus status = ProcessingStatus.PENDING;
    private Map<String, Object> attributes = new HashMap<>(); // 扩展字段

    public enum ProcessingStatus {
        PENDING, START, READING, RUNNING, COMPLETE, FAIL
    }

    public Boolean isFailed() {
        return status == ProcessingStatus.FAIL;
    }
}
