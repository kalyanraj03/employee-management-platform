package com.emp.employeeservice.service;

import com.emp.employeeservice.dto.CreateEmployeeRequest;
import com.emp.employeeservice.dto.EmployeeResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest request);

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse  getMyProfile(String keycloakUserId);

    // List<EmployeeResponse> getAllEmployees();

}