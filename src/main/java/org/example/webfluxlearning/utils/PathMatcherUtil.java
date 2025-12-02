package org.example.webfluxlearning.utils;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于 PathPattern 的路径匹配工具（线程安全 + 缓存）
 */
public class PathMatcherUtil {

    private static final PathPatternParser PARSER = new PathPatternParser();

    // 缓存已解析的模式，避免重复解析（PathPattern 是不可变且线程安全的）
    private static final Map<String, PathPattern> PATTERN_CACHE = new ConcurrentHashMap<>();

    /**
     * 判断 path 是否匹配给定的 pattern（支持 {id}, /** 等）
     */
    public static boolean match(String pattern, String path) {
        PathPattern pathPattern = PATTERN_CACHE.computeIfAbsent(pattern, PARSER::parse);
        return pathPattern.matches(PathContainer.parsePath(path));
    }

    /**
     * 匹配并提取路径变量（如 /user/{id} → {id=123}）
     */
    public static Map<String, String> matchAndExtract(String pattern, String path) {
        PathPattern pathPattern = PATTERN_CACHE.computeIfAbsent(pattern, PARSER::parse);
        var pathContainer = PathContainer.parsePath(path);
        if (pathPattern.matches(pathContainer)) {
            var matchResult = pathPattern.matchAndExtract(pathContainer);
            return matchResult.getUriVariables(); // Map<String, String>
        }
        return null; // 不匹配
    }
}
