package com.auth.repository;

import com.auth.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserCredentialRepository  extends JpaRepository<UserCredential,Integer> {
    Optional<UserCredential> findByFirstName(String username);

    boolean existsByEmail(String email);
}