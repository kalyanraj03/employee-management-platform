package com.emp.employeeservice.service;

import com.emp.employeeservice.client.UserClient;
import com.emp.employeeservice.dto.UserResponse;
import com.emp.employeeservice.exception.UserNotFoundException;
import com.emp.employeeservice.exception.UserServiceUnavailableException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceClient {

    private final UserClient userClient;

    @Retry(
            name = "userService"
    )
    @CircuitBreaker(
            name = "userService",
            fallbackMethod = "userServiceFallback"
    )
    public UserResponse getUser(Long userId) {

        return userClient.getUserById(userId);
    }

    public UserResponse userServiceFallback(
            Long userId,
            Throwable throwable) {

        if (throwable instanceof FeignException.NotFound) {

            throw new UserNotFoundException(
                    "User not found with id: " + userId
            );
        }

        throw new UserServiceUnavailableException(
                "User service is currently unavailable"
        );
    }
}