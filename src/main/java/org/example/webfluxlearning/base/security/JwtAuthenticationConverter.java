package org.example.webfluxlearning.base.security;

import org.example.webfluxlearning.entity.VO.UserAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtAuthenticationConverter implements ServerAuthenticationConverter {
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String urlPath = exchange.getRequest().getPath().value();
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Mono.just(new UsernamePasswordAuthenticationToken(urlPath, authHeader.substring(7)));
        }
        return Mono.just(new AnonymousAuthenticationToken("invalid", urlPath,
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));
    }
}
