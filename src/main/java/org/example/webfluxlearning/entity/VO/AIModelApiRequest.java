package org.example.webfluxlearning.entity.VO;

import lombok.Data;

@Data
public class AIModelApiRequest {
    private String content;
    private String uuid;
    private String modelName;
}
