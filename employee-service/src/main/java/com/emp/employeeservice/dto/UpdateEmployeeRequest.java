package com.emp.employeeservice.dto;

import com.emp.employeeservice.entity.EmployeeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateEmployeeRequest(

        @NotBlank
        String department,

        @NotBlank
        String designation,

        @Positive
        BigDecimal salary,

        EmployeeStatus status

) {}
