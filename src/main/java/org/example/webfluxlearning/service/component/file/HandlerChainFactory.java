package org.example.webfluxlearning.service.component.file;

import org.example.webfluxlearning.base.file.FileHandler;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class HandlerChainFactory {
    private final Map<String, List<FileHandler>> chainMap = new ConcurrentHashMap<>();
    private final List<FileHandler> allHandlers;

    public HandlerChainFactory(List<FileHandler> handlers) {
        allHandlers = handlers.stream()
                .sorted(AnnotationAwareOrderComparator.INSTANCE)
                .collect(Collectors.toList());
        Stream.of("pdf", "txt", "doc").forEach(this::buildForNewChain);
    }

    public List<FileHandler> getHandlerChain(String name) {
        return chainMap.computeIfAbsent(name, this::buildForNewChain);
    }

    private List<FileHandler> buildForNewChain(String name) {
        return allHandlers.stream()
                .filter(handler -> handler.supportType().contains(name) || handler.supportType().contains("*"))
                .toList();
    }
}
