package com.emp.employeeservice.dto;

import com.emp.employeeservice.entity.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeResponse(

        Long id,

        String firstName,

        String lastName,

        String employeeCode,

        String department,

        String designation,

        String email,

        BigDecimal salary,

        LocalDate joiningDate,

        EmployeeStatus status

) {}
