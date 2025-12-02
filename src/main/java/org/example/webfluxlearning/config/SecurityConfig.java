package org.example.webfluxlearning.config;

import org.example.webfluxlearning.base.security.JwtAuthenticationConverter;
import org.example.webfluxlearning.base.security.UserAccessDeniedHandler;
import org.example.webfluxlearning.base.security.UserAuthenticationEntryPoint;
import org.example.webfluxlearning.base.security.anthentication.JwtAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationManager jwtAuthenticationManager;

    @Autowired
    private UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    @Autowired
    private UserAccessDeniedHandler userAccessDeniedHandler;

    private AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new JwtAuthenticationConverter());
//        authenticationWebFilter.setAuthenticationFailureHandler();
        return authenticationWebFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorize -> {
                    authorize.pathMatchers("/api/user/**").permitAll()
                            .anyExchange().authenticated();
                })
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAt(authenticationWebFilter(),  SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(userAuthenticationEntryPoint)
                            .accessDeniedHandler(userAccessDeniedHandler);
                })
                .build();
    }

}
