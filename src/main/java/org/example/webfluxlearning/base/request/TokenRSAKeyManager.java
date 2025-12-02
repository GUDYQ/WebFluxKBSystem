package org.example.webfluxlearning.base.request;

import org.example.webfluxlearning.config.secretkey.JWTConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class TokenRSAKeyManager implements TokenKeyManager<TokenSecretKey>{

    @Autowired
    private JWTConfig jwtConfig;

    private final AtomicReference<TokenSecretKey> currentKeyPair = new AtomicReference<>();


    @Override
    public TokenSecretKey generateSecretKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            LocalDateTime expirationTime = LocalDateTime.now().plusDays(jwtConfig.getExpirationKeyDays()).plusMinutes(5);
            return TokenSecretKey.builder()
                    .publicKey(publicKey)
                    .privateKey(privateKey)
                    .expirationTime(expirationTime)
                    .build();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TokenSecretKey getActivateKey() {
        if ( currentKeyPair.get() == null || currentKeyPair.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            TokenSecretKey nextKey = generateSecretKey();
            this.currentKeyPair.set(nextKey);
        }
        return currentKeyPair.get();
    }

    @Override
    public Boolean rotateKey(String key) {
        TokenSecretKey nextKey = generateSecretKey();
        this.currentKeyPair.set(nextKey);
        return true;
    }

}
