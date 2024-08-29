package com.auth.controller;

import com.auth.entity.RefreshToken;
import com.auth.entity.UserCredential;
import com.auth.model.AuthRequest;
import com.auth.model.JwtResponse;
import com.auth.model.RefreshTokenRequest;
import com.auth.service.AuthService;
import com.auth.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RefreshTokenService refreshTokenService;

    private UserCredential user;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential user) {
        return service.saveUser(user);
    }

//    @PostMapping("/token")
//    public JwtResponse getToken(@RequestBody AuthRequest authRequest) {
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        if (authenticate.isAuthenticated()) {
//           RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
//          return   JwtResponse.builder()
//                    .accessToken(service.generateToken(authRequest.getUsername()))
//                    .token(refreshToken.getToken()).build();
//        } else {
//            throw new RuntimeException("invalid access");
//        }
//    }
@PostMapping("/token")
public JwtResponse getToken(@RequestBody AuthRequest authRequest) {
    try {
        // Authenticate the user
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authenticate.isAuthenticated()) {
            // Check if a refresh token already exists for this user
            RefreshToken existingToken = refreshTokenService.findByUserName(authRequest.getUsername())
                    .orElseGet(() -> refreshTokenService.createRefreshToken(authRequest.getUsername()));

            // Generate new access token
//            String accessToken = service.generateToken(authRequest.getUsername());

            // Return response with new access token and existing or new refresh token
            return JwtResponse.builder()
                    .accessToken(service.generateToken(authRequest.getUsername()))
                    .token(existingToken.getToken())
                    .build();
        } else {
            throw new RuntimeException("Invalid credentials or access denied");
        }
    } catch (Exception e) {
        // Log the exception and provide a meaningful error response
        System.err.println("Error during token generation: " + e.getMessage());
        throw new RuntimeException("Error during token generation");
    }
}

//    @GetMapping("/validate")
//    public String validateToken(@RequestParam("token") String token) {
//        service.validateToken(token);
//        return "Token is valid";
//    }
    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
     return refreshTokenService.findByToken(refreshTokenRequest.getToken())
             .map(refreshTokenService::verifyExpiration)
             .map(RefreshToken::getUserCredential)
             .map(userCredential -> {
                 String accessToken = service.generateToken(userCredential.getFirstName());
                 return JwtResponse.builder()
                         .accessToken(accessToken)
                         .token(refreshTokenRequest.getToken())
                         .build();

             }).orElseThrow(()->new RuntimeException("Refresh Token is not in database"));

    }
//    @DeleteMapping("delete/{id}")
//    @Secured("ROLE_ADMIN")
//    public String deleteById(@PathVariable Integer id){
//        service.deleteUserById(id);
//        return "user deleted";
//    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/dashboard")
    public String getAdminDashboard() {
        return "Admin Dashboard";
    }
}
