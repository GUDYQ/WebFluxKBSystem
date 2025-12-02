package org.example.webfluxlearning.base.request;

import lombok.Builder;
import lombok.Data;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;

@Data
@Builder
public class TokenSecretKey {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private LocalDateTime expirationTime;
}
