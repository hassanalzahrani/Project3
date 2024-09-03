package com.example.project3.Service;

import com.example.project3.Api.ApiException;
import com.example.project3.DTO.CustomerDTO;
import com.example.project3.DTO.EmployeeDTO;
import com.example.project3.Model.Customer;
import com.example.project3.Model.Employee;
import com.example.project3.Model.User;
import com.example.project3.Repository.AuthRepository;
import com.example.project3.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor


public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AuthRepository authRepository;

    // Retrieve all employees
    public List<User> retrieveAllEmployees() {
        List<User> employees = new ArrayList<>();
        for (User user : authRepository.findAll()) {
            if (user.getRole().equalsIgnoreCase("EMPLOYEE")) {
                employees.add(user);
            }
        }
        return employees;
    }

    // Register a new employee
    public void registerNewEmployee(EmployeeDTO employeeDTO) {
        if (!employeeDTO.getRole().equalsIgnoreCase("EMPLOYEE")) {
            throw new ApiException("Invalid role");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(employeeDTO.getPassword());
        employeeDTO.setPassword(hashedPassword);

        User user = new User(null, employeeDTO.getUsername(), hashedPassword, employeeDTO.getName(), employeeDTO.getEmail(), employeeDTO.getRole(), null, null);
        authRepository.save(user);

        Employee employee = new Employee(user.getId(), employeeDTO.getPosition(), employeeDTO.getSalary(), user);
        employeeRepository.save(employee);
    }

    // Update an employee's details
    public void updateEmployeeDetails(int userId, EmployeeDTO employeeDTO) {
        User user = authRepository.findUserById(userId);
        Employee employee = user.getEmployee();

        if (employee == null) {
            throw new ApiException("Employee not found");
        }

        if (user.getId() != employeeDTO.getUserId()) {
            throw new ApiException("Employee ID mismatch");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(employeeDTO.getPassword());
        user.setUsername(employeeDTO.getUsername());
        user.setPassword(hashedPassword);
        user.setName(employeeDTO.getName());
        user.setEmail(employeeDTO.getEmail());
        user.setRole(employeeDTO.getRole());
        authRepository.save(user);

        employee.setPosition(employeeDTO.getPosition());
        employee.setSalary(employeeDTO.getSalary());
        employeeRepository.save(employee);
    }

    // Delete an employee by ID
    public void deleteEmployeeById(int userId, int employeeId) {
        User user = authRepository.findUserById(userId);
        Employee employee = employeeRepository.findEmployeeById(employeeId);

        if (employee == null) {
            throw new ApiException("Employee not found");
        }

        if (user.getId() != employee.getId()) {
            throw new ApiException("Employee ID mismatch");
        }

        employee.setUser(null);
        authRepository.delete(user);
    }
}
