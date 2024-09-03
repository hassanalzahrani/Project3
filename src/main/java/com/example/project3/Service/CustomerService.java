package com.example.project3.Service;

import com.example.project3.Api.ApiException;
import com.example.project3.DTO.CustomerDTO;
import com.example.project3.Model.Customer;
import com.example.project3.Model.User;
import com.example.project3.Repository.AuthRepository;
import com.example.project3.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AuthRepository authRepository;

    // Retrieve all customers from the repository
    public List<User> retrieveAllCustomers() {
        List<User> customers = new ArrayList<>();
        for (User user : authRepository.findAll()) {
            if (user.getRole().equalsIgnoreCase("CUSTOMER")) {
                customers.add(user);
            }
        }
        return customers;
    }

    // Register a new customer
    public void registerNewCustomer(CustomerDTO customerDTO) {
        if (!customerDTO.getRole().equalsIgnoreCase("CUSTOMER")) {
            throw new ApiException("Invalid role");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(customerDTO.getPassword());
        customerDTO.setPassword(hashedPassword);

        User user = new User(null, customerDTO.getUsername(), hashedPassword, customerDTO.getName(), customerDTO.getEmail(), customerDTO.getRole(), null, null);
        authRepository.save(user);

        Customer customer = new Customer(user.getId(), customerDTO.getPhoneNumber(), user, null);
        user.setCustomer(customer);
        customerRepository.save(customer);
    }

    // Update existing customer details
    public void updateCustomerDetails(int userId, CustomerDTO customerDTO) {
        User user = authRepository.findUserById(userId);
        if (user == null || user.getCustomer() == null) {
            throw new ApiException("Customer not found");
        }

        if (user.getId() != customerDTO.getUserId()) {
            throw new ApiException("Customer ID mismatch");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(customerDTO.getPassword());
        user.setUsername(customerDTO.getUsername());
        user.setPassword(hashedPassword);
        user.setName(customerDTO.getName());
        user.setEmail(customerDTO.getEmail());
        user.setRole(customerDTO.getRole());
        authRepository.save(user);

        Customer customer = user.getCustomer();
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customerRepository.save(customer);
    }

    // Delete a customer by ID
    public void deleteCustomerById(int userId, int customerId) {
        User user = authRepository.findUserById(userId);
        Customer customer = customerRepository.findCustomerById(customerId);

        if (customer == null || user == null || user.getId() != customer.getId()) {
            throw new ApiException("Customer not found or ID mismatch");
        }

        customer.setUser(null);
        authRepository.delete(user);
    }
}
