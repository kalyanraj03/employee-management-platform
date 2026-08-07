package com.emp.userservice.controller;

import com.emp.userservice.dto.LoginRequest;
import com.emp.userservice.dto.LoginResponse;
import com.emp.userservice.security.CustomUserDetails;
import com.emp.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        return authService.login(request);

    }

    @GetMapping("/me")
    public ResponseEntity<?> me(
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(
                Map.of(
                        "email", user.getUsername(),
                        "role", user.getAuthorities()
                )
        );
    }
}