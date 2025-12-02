package org.example.webfluxlearning.base.security.anthentication;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public class DelegateAuthenticationManager implements ReactiveAuthenticationManager {

    private final Class<? extends Authentication> supportClass;

    private final ReactiveAuthenticationManager manager;

    public DelegateAuthenticationManager(Class<? extends Authentication> supportClass,
                                          ReactiveAuthenticationManager manager) {
        this.manager = manager;
        this.supportClass = supportClass;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (!supportClass.isAssignableFrom(authentication.getClass())) {
            return Mono.empty();
        }

        return manager.authenticate(authentication);
    }
}