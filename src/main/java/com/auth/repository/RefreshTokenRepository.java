package com.auth.repository;

import com.auth.entity.RefreshToken;
import com.auth.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserCredential(UserCredential user);
}
