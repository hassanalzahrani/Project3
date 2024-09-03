package com.example.project3.Controller;


import com.example.project3.DTO.EmployeeDTO;
import com.example.project3.Model.Employee;
import com.example.project3.Model.User;
import com.example.project3.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor


public class EmployeeController {

    private final EmployeeService employeeService;

    // Get all employees
    @GetMapping("/employees")
    public ResponseEntity fetchAllEmployees() {
        return ResponseEntity.status(200).body(employeeService.retrieveAllEmployees());
    }

    // Register a new employee
    @PostMapping("/register")
    public ResponseEntity addNewEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        employeeService.registerNewEmployee(employeeDTO);
        return ResponseEntity.status(200).body("Employee registered successfully");
    }

    // Update an employee's details
    @PutMapping("/update")
    public ResponseEntity modifyEmployee(@AuthenticationPrincipal User user, @Valid @RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployeeDetails(user.getId(), employeeDTO);
        return ResponseEntity.status(200).body("Employee updated successfully");
    }

    // Delete an employee by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity removeEmployee(@AuthenticationPrincipal User user, @PathVariable int id) {
        employeeService.deleteEmployeeById(user.getId(), id);
        return ResponseEntity.status(200).body("Employee deleted successfully");
    }
}