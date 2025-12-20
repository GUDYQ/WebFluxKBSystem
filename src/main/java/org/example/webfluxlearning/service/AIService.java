package org.example.webfluxlearning.service;

import org.example.webfluxlearning.base.response.ResponseVO;
import org.example.webfluxlearning.entity.VO.AIModelApiRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AIService {
    Flux<String> chat(AIModelApiRequest request);
}
