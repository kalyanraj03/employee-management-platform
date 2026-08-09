package com.emp.employeeservice.util;

import com.emp.employeeservice.dto.CreateEmployeeRequest;
import com.emp.employeeservice.dto.EmployeeResponse;
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

    public EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getUserId(),
                employee.getEmployeeCode(),
                employee.getDepartment(),
                employee.getDesignation(),
                employee.getSalary(),
                employee.getJoiningDate(),
                employee.getStatus(),
                employee.getCreatedAt()
        );    }
}