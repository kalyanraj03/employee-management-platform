package com.emp.userservice.dto;

import com.emp.userservice.entity.Role;

import java.time.LocalDateTime;

public record UserResponse(

        Long id,

        String firstName,

        String lastName,

        String email,

        Role role,

        LocalDateTime createdAt

) {
}