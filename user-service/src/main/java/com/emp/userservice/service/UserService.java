package com.emp.userservice.service;

import com.emp.userservice.dto.CreateUserRequest;
import com.emp.userservice.dto.UpdateUserRequest;
import com.emp.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse registerUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);

}