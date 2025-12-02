package org.example.webfluxlearning.base.security.anthentication;

import org.example.webfluxlearning.service.UserAuthenticationService;
import org.example.webfluxlearning.utils.PBKDF2Util;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CustomAuthenticationManager {
    private final PBKDF2Util pbkdf2Util;
    private final UserAuthenticationService userAuthenticationService;
    private final List<ReactiveAuthenticationManager> managers;

    public CustomAuthenticationManager(PBKDF2Util pbkdf2Util, UserAuthenticationService userAuthenticationService) {
        this.pbkdf2Util = pbkdf2Util;
        this.userAuthenticationService = userAuthenticationService;
        UserDetailsRepositoryReactiveAuthenticationManager repositoryAuthenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userAuthenticationService);
        repositoryAuthenticationManager.setPasswordEncoder(pbkdf2Util.getPasswordEncoder());
        ReactiveAuthenticationManager passwordAuthenticationManager =
                new DelegateAuthenticationManager(UsernamePasswordAuthenticationToken.class, repositoryAuthenticationManager);
        this.managers = List.of(passwordAuthenticationManager);
    }

    public Mono<Authentication> authenticate(Authentication auth) {
        return Flux.fromIterable(managers)
                .concatMap(mgr -> mgr.authenticate(auth))
                .next()
                .switchIfEmpty(Mono.error(new AuthenticationServiceException("Unsupported token")));
    }
}
