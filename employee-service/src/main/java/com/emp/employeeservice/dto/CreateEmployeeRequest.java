package com.emp.employeeservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEmployeeRequest(

        @NotNull
        Long userId,

        @NotBlank
        String department,

        @NotBlank
        String designation,

        @Positive
        BigDecimal salary,

        @PastOrPresent
        LocalDate joiningDate

) {}
