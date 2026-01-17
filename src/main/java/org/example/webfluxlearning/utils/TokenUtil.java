package org.example.webfluxlearning.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.example.webfluxlearning.base.exception.login.TokenErrorException;
import org.example.webfluxlearning.config.secretkey.JWTConfig;
import org.example.webfluxlearning.entity.VO.AccessTokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenUtil {

    @Autowired
    private JWTConfig jwtConfig;

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public String generateAccessToken(UUID userId, String username, String email, RSAPrivateKey privateKey) throws TokenErrorException{
        try {
            // 创建JWT声明集
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, jwtConfig.getExpirationMinutes()); // Token valid for 30 minutes
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userId.toString()) // 通常用userId作为subject
                    .issuer("TestLearning") // 发行者
                    .expirationTime(calendar.getTime())
                    .notBeforeTime(new Date())
                    .issueTime(new Date())
                    .jwtID(UUID.randomUUID().toString())
                    .claim("username", username)
                    .claim("email", email)
                    .build();

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .type(JOSEObjectType.JWT)
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            JWSSigner signer = new RSASSASigner(privateKey);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new TokenErrorException("Error generating JWT: " + e.getMessage());
        }
    }

    public JWTClaimsSet verifyAccessToken(String token, RSAPublicKey publicKey) throws TokenErrorException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(publicKey);

            if (!signedJWT.verify(verifier)) {
                throw new TokenErrorException("Invalid JWT signature");
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            Date expirationTime = claimsSet.getExpirationTime();
            if (expirationTime == null || expirationTime.before(new Date())) {
                throw new TokenErrorException("Token has expired");
            }

            Date notBeforeTime = claimsSet.getNotBeforeTime();
            if (notBeforeTime != null && notBeforeTime.after(new Date())) {
                throw new TokenErrorException("Token not valid yet");
            }

            return claimsSet;
        } catch (Exception  e) {
            throw new TokenErrorException("JWT verification failed: " + e.getMessage());
        }
    }

    public AccessTokenInfo parseAccessToken(String token, RSAPublicKey publicKey) throws TokenErrorException {
        try {
            JWTClaimsSet claimsSet = verifyAccessToken(token, publicKey);
            String userId = claimsSet.getSubject(); // 从subject获取userId
            String username = claimsSet.getStringClaim("username");
            String email = claimsSet.getStringClaim("email");

            return new AccessTokenInfo(userId, username, email);
        } catch (TokenErrorException | ParseException e) {
            throw new TokenErrorException(e.getMessage());
        }
    }

}

