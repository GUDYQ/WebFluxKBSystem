package org.example.webfluxlearning.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIModelApiResponse {
    private String content;
    private FileType type;

    enum FileType {TEXT, PICTURE, VIDEO};

    public static AIModelApiResponse text(String content) {
        return new AIModelApiResponse(content, FileType.TEXT);
    }

    public static AIModelApiResponse picture(String content) {
        return new AIModelApiResponse(content, FileType.PICTURE);
    }

    public static AIModelApiResponse video(String content) {
        return new AIModelApiResponse(content, FileType.VIDEO);
    }
}
