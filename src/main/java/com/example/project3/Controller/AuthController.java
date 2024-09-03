package com.example.project3.Controller;


import com.example.project3.DTO.CustomerDTO;
import com.example.project3.Model.User;
import com.example.project3.Service.AuthService;
import com.example.project3.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Retrieve all users
    @GetMapping("/users")
    public ResponseEntity getAllUsers() {
        return ResponseEntity.status(200).body(authService.fetchAllUsers());
    }

    // Delete a user by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity removeUser(@PathVariable Integer id) {
        authService.deleteUserById(id);
        return ResponseEntity.status(200).body("User deleted successfully");
    }
}