package com.emp.userservice.controller;

import com.emp.userservice.dto.CreateUserRequest;
import com.emp.userservice.dto.UpdateUserRequest;
import com.emp.userservice.dto.UserResponse;
import com.emp.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody CreateUserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerUser(request));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<UserResponse> getUserById(
//            @PathVariable Long id) {
//
//        return ResponseEntity.ok(userService.getUserById(id));
//    }

    @GetMapping("/{sessId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable String sessId) {

        return ResponseEntity.ok(userService.getUserBysessId(sessId));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}