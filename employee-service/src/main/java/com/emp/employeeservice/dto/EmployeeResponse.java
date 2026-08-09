package com.emp.employeeservice.dto;

import com.emp.employeeservice.entity.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeResponse(

        Long id,

        Long userId,

        String employeeCode,

        String department,

        String designation,

        BigDecimal salary,

        LocalDate joiningDate,

        EmployeeStatus status,

        LocalDateTime createdAt

) {}
