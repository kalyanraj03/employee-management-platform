package com.emp.employeeservice.service.serviceImpl;

import com.emp.employeeservice.dto.CreateEmployeeRequest;
import com.emp.employeeservice.dto.EmployeeResponse;
import com.emp.employeeservice.entity.Employee;
import com.emp.employeeservice.entity.EmployeeStatus;
import com.emp.employeeservice.exception.EmployeeAlreadyExistsException;
import com.emp.employeeservice.exception.EmployeeNotFoundException;
import com.emp.employeeservice.repository.EmployeeRepository;
import com.emp.employeeservice.service.EmployeeService;
import com.emp.employeeservice.util.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {

        if (employeeRepository.existsByUserId(request.userId())) {
            throw new EmployeeAlreadyExistsException(
                    "Employee already exists for user id : " + request.userId());
        }

        Employee employee = employeeMapper.toEntity(request);

        employee.setEmployeeCode(generateEmployeeCode());
        employee.setStatus(EmployeeStatus.ACTIVE);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee not found with id : " + id));

        return employeeMapper.toResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    private String generateEmployeeCode() {

        long count = employeeRepository.count() + 1;

        return String.format("EMP%04d", count);
    }
}