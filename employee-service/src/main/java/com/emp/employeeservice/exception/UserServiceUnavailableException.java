package com.emp.employeeservice.exception;

public class UserServiceUnavailableException
        extends RuntimeException {

    public UserServiceUnavailableException(String message) {
        super(message);
    }
}