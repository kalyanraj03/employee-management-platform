package com.emp.userservice.service;

import com.emp.userservice.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> login(LoginRequest request);

}