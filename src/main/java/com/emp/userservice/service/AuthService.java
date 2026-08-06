package com.emp.userservice.service;

import com.emp.userservice.dto.LoginRequest;
import com.emp.userservice.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}