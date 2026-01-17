package org.example.webfluxlearning.base.request;

import org.example.webfluxlearning.base.exception.login.TokenErrorException;
import org.example.webfluxlearning.entity.VO.AccessTokenInfo;
import org.example.webfluxlearning.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserTokenManager {

    @Autowired
    private TokenRSAKeyManager tokenRSAKeyManager;

    @Autowired
    private TokenUtil tokenUtil;

    public String generateTokens(UUID userId, String username, String email) {
        TokenSecretKey activateKey = tokenRSAKeyManager.getActivateKey();
        // 验证 RefreshToken, 在数据库中获取， 同时修改相关需要更新
        return tokenUtil.generateAccessToken(userId, username, email, activateKey.getPrivateKey());
    }

    public AccessTokenInfo getUserInfoToken(String token) throws TokenErrorException {
        TokenSecretKey activateKey = tokenRSAKeyManager.getActivateKey();
        tokenUtil.verifyAccessToken(token, activateKey.getPublicKey());
        return tokenUtil.parseAccessToken(token, activateKey.getPublicKey());
    }

    public String generateRefreshToken(UUID userId) {
        // 保存刷新密钥
        return tokenUtil.generateRefreshToken();
    }
}