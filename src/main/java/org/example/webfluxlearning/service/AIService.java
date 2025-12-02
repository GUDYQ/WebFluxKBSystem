package org.example.webfluxlearning.service;

import org.example.webfluxlearning.base.response.ResponseVO;
import org.example.webfluxlearning.entity.VO.AIModelApiRequest;
import reactor.core.publisher.Mono;

public interface AIService {
    Mono<String> chat(AIModelApiRequest request);
}
