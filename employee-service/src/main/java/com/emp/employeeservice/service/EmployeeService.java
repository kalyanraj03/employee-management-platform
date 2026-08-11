package com.emp.employeeservice.service;

import com.emp.employeeservice.dto.CreateEmployeeRequest;
import com.emp.employeeservice.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest request);

    EmployeeResponse getEmployeeById(Long id);

   // List<EmployeeResponse> getAllEmployees();

}