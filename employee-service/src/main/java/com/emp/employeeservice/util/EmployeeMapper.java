package com.emp.employeeservice.util;

import com.emp.employeeservice.dto.CreateEmployeeRequest;
import com.emp.employeeservice.dto.EmployeeResponse;
import com.emp.employeeservice.dto.UserResponse;
import com.emp.employeeservice.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {


    public Employee toEntity(CreateEmployeeRequest request) {
        return Employee.builder()
                .userId(request.userId())
                .department(request.department())
                .designation(request.designation())
                .salary(request.salary())
                .joiningDate(request.joiningDate())
                .build();
    }

    public EmployeeResponse toResponse(Employee employee, UserResponse user) {
        return new EmployeeResponse(
                employee.getId(),
                user.firstName(),
                user.lastName(),
                employee.getEmployeeCode(),
                employee.getDepartment(),
                employee.getDesignation(),
                user.email(),
                employee.getSalary(),
                employee.getJoiningDate(),
                employee.getStatus()
        );
    }
}