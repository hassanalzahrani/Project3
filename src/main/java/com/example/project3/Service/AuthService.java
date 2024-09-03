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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    // Fetch all users from the repository
    public List<User> fetchAllUsers() {
        return authRepository.findAll();
    }

    // Delete a user by ID
    public void deleteUserById(int id) {
        User user = authRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }
        authRepository.delete(user);
    }
}