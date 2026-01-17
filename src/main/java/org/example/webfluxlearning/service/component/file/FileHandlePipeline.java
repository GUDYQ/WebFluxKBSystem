package org.example.webfluxlearning.service.component.file;

import org.example.webfluxlearning.base.file.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

// 文件按的处理链？
@Component
public class FileHandlePipeline {
    @Autowired
    private HandlerChainFactory chainFactory;

    public Mono<Boolean> handle() {
//        chainFactory.getHandlerChain();
        return null;
    }
}
