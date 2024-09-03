package com.example.project3.Controller;

import com.example.project3.DTO.CustomerDTO;
import com.example.project3.Model.Customer;
import com.example.project3.Model.User;
import com.example.project3.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // Fetch all customers
    @GetMapping("/customers")
    public ResponseEntity fetchAllCustomers() {
        return ResponseEntity.status(200).body(customerService.retrieveAllCustomers());
    }

    // Register a new customer
    @PostMapping("/register")
    public ResponseEntity addNewCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        customerService.registerNewCustomer(customerDTO);
        return ResponseEntity.status(200).body("Customer registered successfully");
    }

    // Update customer information
    @PutMapping("/update")
    public ResponseEntity modifyCustomer(@AuthenticationPrincipal User user, @Valid @RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomerDetails(user.getId(), customerDTO);
        return ResponseEntity.status(200).body("Customer updated successfully");
    }

    // Delete a customer by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity removeCustomer(@AuthenticationPrincipal User user, @PathVariable int id) {
        customerService.deleteCustomerById(user.getId(), id);
        return ResponseEntity.status(200).body("Customer deleted successfully");
    }
}