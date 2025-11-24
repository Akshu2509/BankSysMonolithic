package com.example.demo.controller;


import java.util.HashMap;
import java.util.*;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.InsufficientBalanceException;
import com.example.demo.exception.InvalidAmountException;
import com.example.demo.model.Account;
import com.example.demo.model.DTOs.*;
import com.example.demo.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/test")
    public String testDB() {
        List<String> dbs = mongoTemplate.getDb().listCollectionNames().into(new ArrayList<>());
        System.out.println("Collections: " + dbs);
        return "Collections: " + dbs;
    }

    // Create account
    @PostMapping("/add")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account created = accountService.createAccount(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
 // Get all accounts
    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }


    // Get account
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccount(accountNumber));
    }

    // Deposit
    @PutMapping("/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositRequest request) {

        return ResponseEntity.ok(accountService.deposit(accountNumber, request));
    }

    // Withdraw
    @PutMapping("/{accountNumber}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody WithdrawRequest request) {

        return ResponseEntity.ok(accountService.withdraw(accountNumber, request));
    }

    // Transfer
//    @PostMapping("/transfer")
//    public ResponseEntity<String> transfer(
//            @Valid @RequestBody TransferRequest request) {
//
//        return ResponseEntity.ok(accountService.transfer(request));
//    }
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {

        // 1️⃣ Check if source and destination are the same
        if (request.getSourceAccount().equals(request.getDestinationAccount())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Source and destination cannot be the same");
            return ResponseEntity.badRequest().body(errorResponse);
        }
//        // 2️⃣ Check if source account exists
//        Account source = accountService.getAccount(request.getSourceAccount());
//        if (source == null) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("error", "Source account not found");
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//
//        // 3️⃣ Check if destination account exists
//        Account dest = accountService.getAccount(request.getDestinationAccount());
//        if (dest == null) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("error", "Destination account not found");
//            return ResponseEntity.badRequest().body(errorResponse);
//        }

        try {
            // 2️⃣ Use the service method that already exists
            String result = accountService.transfer(request);
            return ResponseEntity.ok(result);

        } catch (AccountNotFoundException | InsufficientBalanceException | InvalidAmountException e) {
            // 3️⃣ Handle custom exceptions
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            // 4️⃣ Catch-all for unexpected errors
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Something went wrong: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }


    // Delete account
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.ok("Account deleted successfully");
    }
}
