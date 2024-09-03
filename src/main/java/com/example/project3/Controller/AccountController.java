package com.example.project3.Controller;

import com.example.project3.Model.Account;
import com.example.project3.Model.User;
import com.example.project3.Service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    // Get all accounts
    @GetMapping("/get-all-accounts")
    public ResponseEntity getAllAccounts() {
        return ResponseEntity.status(200).body(accountService.getAllAccounts());
    }

    // Add a new account for the authenticated user
    @PostMapping("/add-account")
    public ResponseEntity createAccount(@AuthenticationPrincipal User user, @Valid @RequestBody Account account) {
        int userId = user.getId();
        accountService.createAccount(userId, account);
        return ResponseEntity.status(200).body("Account created successfully");
    }

    // Update an existing account
    @PutMapping("/update-account/{accountId}")
    public ResponseEntity modifyAccount(@AuthenticationPrincipal User user, @Valid @RequestBody Account account, @PathVariable int accountId) {
        accountService.modifyAccount(user.getId(), account, accountId);
        return ResponseEntity.status(200).body("Account updated successfully");
    }

    // Delete an account
    @DeleteMapping("/delete-account/{accountId}")
    public ResponseEntity removeAccount(@AuthenticationPrincipal User user, @PathVariable int accountId) {
        accountService.removeAccount(user.getId(), accountId);
        return ResponseEntity.status(200).body("Account deleted successfully");
    }

    // Activate an account
    @PutMapping("/activate-account/{accountId}")
    public ResponseEntity activateAccount(@AuthenticationPrincipal User user, @PathVariable int accountId) {
        accountService.activateAccount(user.getId(), accountId);
        return ResponseEntity.status(200).body("Account activated successfully");
    }

    // View account details
    @GetMapping("/view-account-details/{accountId}")
    public ResponseEntity getAccountDetails(@AuthenticationPrincipal User user, @PathVariable int accountId) {
        return ResponseEntity.status(200).body(accountService.getAccountDetails(user.getId(), accountId));
    }

    // Get all accounts for the authenticated user
    @GetMapping("/get-user-accounts")
    public ResponseEntity getUserAccounts(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(accountService.getUserAccounts(user.getId()));
    }

    // Deposit money into an account
    @PutMapping("/deposit/{accountId}/{amount}")
    public ResponseEntity depositMoney(@AuthenticationPrincipal User user, @PathVariable int accountId, @PathVariable int amount) {
        accountService.depositMoney(user.getId(), accountId, amount);
        return ResponseEntity.status(200).body("Money deposited successfully");
    }

    // Withdraw money from an account
    @PutMapping("/withdraw/{accountId}/{amount}")
    public ResponseEntity withdrawMoney(@AuthenticationPrincipal User user, @PathVariable int accountId, @PathVariable int amount) {
        accountService.withdrawMoney(user.getId(), accountId, amount);
        return ResponseEntity.status(200).body("Money withdrawn successfully");
    }

    // Transfer money between accounts
    @PutMapping("/transfer/{fromAccountId}/{toAccountId}/{amount}")
    public ResponseEntity transferMoney(@AuthenticationPrincipal User user, @PathVariable int fromAccountId, @PathVariable int toAccountId, @PathVariable int amount) {
        accountService.transferMoney(user.getId(), fromAccountId, toAccountId, amount);
        return ResponseEntity.status(200).body("Money transferred successfully");
    }

    // Block an account
    @PutMapping("/block-account/{accountId}")
    public ResponseEntity blockAccount(@AuthenticationPrincipal User user, @PathVariable int accountId) {
        accountService.blockAccount(user.getId(), accountId);
        return ResponseEntity.status(200).body("Account blocked successfully");
    }
}
