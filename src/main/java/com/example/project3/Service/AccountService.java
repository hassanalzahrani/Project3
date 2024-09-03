package com.example.project3.Service;

import com.example.project3.Api.ApiException;
import com.example.project3.Model.Account;
import com.example.project3.Model.Customer;
import com.example.project3.Model.User;
import com.example.project3.Repository.AccountRepository;
import com.example.project3.Repository.AuthRepository;
import com.example.project3.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthRepository authRepository;
    private final CustomerRepository customerRepository;

    // Fetch all accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Create a new account for a customer
    public void createAccount(int userId, Account account) {
        User user = authRepository.findUserById(userId);
        if (!user.getRole().equalsIgnoreCase("CUSTOMER")) {
            throw new ApiException("You are not authorized to create an account");
        }
        Customer customer = customerRepository.findCustomerById(user.getId());
        account.setCustomer(customer);
        accountRepository.save(account);
    }

    // Update an existing account
    public void modifyAccount(int userId, Account updatedAccount, int accountId) {
        Account existingAccount = accountRepository.findAccountById(accountId);
        if (existingAccount == null) {
            throw new ApiException("Account not found");
        }
        if (existingAccount.getCustomer().getId() != userId) {
            throw new ApiException("You are not authorized to update this account");
        }
        existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
        existingAccount.setActive(updatedAccount.isActive());
        existingAccount.setBalance(updatedAccount.getBalance());
        accountRepository.save(existingAccount);
    }

    // Delete an account
    public void removeAccount(int userId, int accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (account.getCustomer().getId() != userId) {
            throw new ApiException("You are not authorized to delete this account");
        }
        Customer customer = account.getCustomer();
        customer.getAccounts().remove(account);
        accountRepository.delete(account);
    }

    // Activate an account
    public void activateAccount(int userId, int accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (account.isActive()) {
            throw new ApiException("Account is already active");
        }
        if (account.getCustomer().getId() != userId) {
            throw new ApiException("You are not authorized to activate this account");
        }
        account.setActive(true);
        accountRepository.save(account);
    }

    // View account details
    public Account getAccountDetails(int userId, int accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (account.getCustomer().getId() != userId) {
            throw new ApiException("You are not authorized to view this account's details");
        }
        return account;
    }

    // Get all accounts of the authenticated user
    public List<Account> getUserAccounts(int userId) {
        Customer customer = customerRepository.findCustomerById(userId);
        return new ArrayList<>(customer.getAccounts());
    }

    // Deposit money into an account
    public void depositMoney(int userId, int accountId, int amount) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (!account.isActive()) {
            throw new ApiException("Account is not active");
        }
        if (account.getCustomer().getId() != userId) {
            throw new ApiException("You are not authorized to deposit into this account");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    // Withdraw money from an account
    public void withdrawMoney(int userId, int accountId, int amount) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (!account.isActive()) {
            throw new ApiException("Account is not active");
        }
        if (account.getCustomer().getId() != userId) {
            throw new ApiException("You are not authorized to withdraw from this account");
        }
        if (account.getBalance() < amount) {
            throw new ApiException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    // Transfer money between accounts
    public void transferMoney(int userId, int fromAccountId, int toAccountId, int amount) {
        Account fromAccount = accountRepository.findAccountById(fromAccountId);
        Account toAccount = accountRepository.findAccountById(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new ApiException("One or both accounts not found");
        }
        if (!fromAccount.isActive() || !toAccount.isActive()) {
            throw new ApiException("One or both accounts are not active");
        }
        if (fromAccount.getCustomer().getId() != userId) {
            throw new ApiException("You are not authorized to transfer from this account");
        }
        if (fromAccount.getBalance() < amount) {
            throw new ApiException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    // Block an account
    public void blockAccount(int userId, int accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (account.getCustomer().getId() != userId) {
            throw new ApiException("You are not authorized to block this account");
        }
        account.setActive(false);
        accountRepository.save(account);
    }
}
