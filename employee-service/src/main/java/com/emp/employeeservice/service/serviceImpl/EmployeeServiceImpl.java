package com.emp.employeeservice.service.serviceImpl;

import com.emp.employeeservice.client.UserClient;
import com.emp.employeeservice.dto.EmployeeResponse;
import com.emp.employeeservice.dto.UserResponse;
import com.emp.employeeservice.dto.CreateEmployeeRequest;
import com.emp.employeeservice.entity.Employee;
import com.emp.employeeservice.entity.EmployeeStatus;
import com.emp.employeeservice.exception.EmployeeAlreadyExistsException;
import com.emp.employeeservice.exception.EmployeeNotFoundException;
import com.emp.employeeservice.exception.UserNotFoundException;
import com.emp.employeeservice.exception.UserServiceUnavailableException;
import com.emp.employeeservice.repository.EmployeeRepository;
import com.emp.employeeservice.service.EmployeeService;
import com.emp.employeeservice.service.UserServiceClient;
import com.emp.employeeservice.util.EmployeeMapper;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {

        userServiceClient.getUser(request.userId());

        if (employeeRepository.existsByUserId(request.userId())) {
            throw new EmployeeAlreadyExistsException(
                    "Employee already exists for user id : " + request.userId());
        }

        Employee employee = employeeMapper.toEntity(request);

        employee.setEmployeeCode(generateEmployeeCode());
        employee.setStatus(EmployeeStatus.ACTIVE);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponse(savedEmployee, new UserResponse(null, null, null, null, null));
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee not found with id : " + id));

        UserResponse user = userServiceClient.getUser(employee.getUserId());

        return employeeMapper.toResponse(employee, user);
    }

//    @Override
//    public List<EmployeeResponse> getAllEmployees() {
//
//        return employeeRepository.findAll()
//                .stream()
//                .map(employeeMapper::toResponse)
//                .toList();
//    }

    private String generateEmployeeCode() {

        long count = employeeRepository.count() + 1;

        return String.format("EMP%04d", count);
    }

}