package com.auth.service;

import com.auth.entity.RefreshToken;
import com.auth.entity.UserCredential;
import com.auth.repository.RefreshTokenRepository;
import com.auth.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository tokenRepository;
    @Autowired
    private UserCredentialRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .userCredential(userRepository.findByFirstName(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return tokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return tokenRepository.findByToken(token);
    }
    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            tokenRepository.delete(token);
            throw new RuntimeException(token.getToken() +"Refresh Token was expired");
        }
        return token;
    }
    public Optional<RefreshToken> findByUserName(String username) {
        UserCredential user = userRepository.findByFirstName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return tokenRepository.findByUserCredential(user);
    }
}