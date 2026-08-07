package com.emp.userservice.service.serviceImpl;

import com.emp.userservice.dto.LoginRequest;
import com.emp.userservice.dto.LoginResponse;
import com.emp.userservice.security.CustomUserDetails;
import com.emp.userservice.security.CustomUserDetailsService;
import com.emp.userservice.security.JwtService;
import com.emp.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public ResponseEntity<?> login(LoginRequest request) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        if (authenticate.isAuthenticated()) {

            CustomUserDetails userDetails =
                    (CustomUserDetails) customUserDetailsService
                            .loadUserByUsername(request.email());

            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponse(token, "Bearer"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

    }
}