package org.example.webfluxlearning.base.security.anthentication;

import org.example.webfluxlearning.base.exception.login.TokenErrorException;
import org.example.webfluxlearning.base.request.UserTokenManager;
import org.example.webfluxlearning.dao.repository.UserRepository;
import org.example.webfluxlearning.entity.VO.AccessTokenInfo;
import org.example.webfluxlearning.service.UserAuthenticationService;
import org.example.webfluxlearning.utils.PathMatcherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private UserTokenManager tokenManagerService;

    private final List<String> ignorePaths = List.of("/api/user/login");

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String urlPath = authentication.getPrincipal().toString();
//        System.out.println(token);

        if (token == null || token.isEmpty()) {
//            System.out.println(ignorePaths.stream().anyMatch(pattern -> PathMatcherUtil.match(pattern, urlPath)));
            System.out.println(urlPath);
            if (ignorePaths.stream().anyMatch(pattern -> PathMatcherUtil.match(pattern, urlPath))) {
                var anonymousAuthentication = new AnonymousAuthenticationToken("invalid", "invalid",
                        AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
                return Mono.just(anonymousAuthentication);
            } else {
                return Mono.error(new BadCredentialsException("Invalid token"));
            }
        }
        return Mono.<Authentication>fromCallable(() -> {
                    AccessTokenInfo tokenInfo = tokenManagerService.getUserInfoToken(token);
            return new UsernamePasswordAuthenticationToken(tokenInfo.getUserId(), tokenInfo,
                    AuthorityUtils.createAuthorityList("ROLE_USER"));
        }).onErrorResume(e -> {
            System.out.println(e.getMessage());
                if(ignorePaths.stream().anyMatch(pattern -> PathMatcherUtil.match(pattern, urlPath))) {
                    var anonymousAuthentication = new AnonymousAuthenticationToken("invalid", "invalid",
                            AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
                    System.out.println("anonymousAuthentication");
                    return Mono.just(anonymousAuthentication);
                }
                else {
                    System.out.println("Error");
                    return Mono.error(new BadCredentialsException("Invalid token"));
                }
        });
    }
}
