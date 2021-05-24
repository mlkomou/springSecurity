/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.jwtsecurite.controller;

import io.tuwindi.demo.payload.JwtAuthenticationResponse;
import io.tuwindi.demo.payload.LoginForm;
import io.tuwindi.demo.sec.JwtTokenProvider;
import io.tuwindi.demo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 * @author alhousseini
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private AuthService authService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userDetails.getAuthorities()));
    }

    @GetMapping("/{login}/{password}")
    public ResponseEntity<?> authentication(@PathVariable(name = "login", required = true) String login, @PathVariable(name = "password", required = true) String password) {
        return new ResponseEntity<>(authService.login(login, password), HttpStatus.OK);
    }
    
    @PutMapping("/update/password/{userId}/{oldPassword}/{newPassword}")
    public ResponseEntity<?> updatePassword(@PathVariable(name = "userId", required = true) Long userId, @PathVariable(name = "oldPassword", required = true) String oldPassword, @PathVariable(name = "newPassword", required = true) String newPassword) {
        return new ResponseEntity<>(authService.updatePassword(userId, oldPassword, newPassword), HttpStatus.OK);
    }

    @PutMapping("/reset/password/{userId}/{newPassword}")
    public ResponseEntity<?> resetPassword(@PathVariable(name = "userId", required = true) Long userId, @PathVariable(name = "newPassword", required = true) String newPassword) {
        return new ResponseEntity<>(authService.resetPassword(userId, newPassword), HttpStatus.OK);
    }
}
