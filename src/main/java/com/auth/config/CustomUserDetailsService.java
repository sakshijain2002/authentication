package com.auth.config;


import com.auth.entity.Role;
import com.auth.entity.UserCredential;

import com.auth.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserCredentialRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> credential = repository.findByFirstName(username);
        return credential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Load the user and their roles from the database
//        return repository.findByFirstName(username)
//                .map(user -> new org.springframework.security.core.userdetails.User(
//                        user.getFirstName(),
//                        user.getPassword(),
//                        user.getRole().stream()
//                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
//                                .collect(Collectors.toList())))
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
}
