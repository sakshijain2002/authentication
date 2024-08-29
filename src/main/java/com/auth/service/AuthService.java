package com.auth.service;


import com.auth.entity.Role;
import com.auth.entity.UserCredential;

import com.auth.repository.RoleRepository;
import com.auth.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;


//    public String saveUser(UserCredential credential) {
//
//        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
//        repository.save(credential);
//        return "user added to the system";
//    }
public String saveUser(UserCredential userCredential) {
    // Encrypt the password
    userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
    if (repository.existsByEmail(userCredential.getEmail())) {
        throw new RuntimeException("Email address already exists.");
    }

    // Assign roles to the user
    Set<Role> roles = userCredential.getRole();
    if (roles != null) {
        for (Role role : roles) {
            // Ensure roles exist in the database
            Role existingRole = roleRepository.findByRole(role.getRole());
            if (existingRole != null) {
                role.setId(existingRole.getId());
            } else {
                // Handle the case where the role does not exist
                // You might want to create and save the role if necessary
            }
        }
    }

    // Save the user with roles
    repository.save(userCredential);
    return "User registered successfully";
}

    public void deleteUserById(Integer id){
        repository.deleteById(id);
    }



    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }


}