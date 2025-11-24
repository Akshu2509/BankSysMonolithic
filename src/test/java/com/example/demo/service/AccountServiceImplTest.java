package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.InvalidAmountException;
import com.example.demo.model.Account;
import com.example.demo.model.DTOs.CreateAccountRequest;
import com.example.demo.model.DTOs.DepositRequest;
import com.example.demo.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test createAccount
    @Test
    void testCreateAccount() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setHolderName("Akshara");

        Account savedAccount = Account.builder()
                .accountNumber("AKS1234")
                .holderName("Akshara")
                .balance(0.0)
                .status("ACTIVE")
                .createdAt(new Date())
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        Account result = accountService.createAccount(request);

        assertNotNull(result);
        assertEquals("Akshara", result.getHolderName());
        assertEquals(0.0, result.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    // Test getAccount - success
    @Test
    void testGetAccountSuccess() {
        Account account = Account.builder()
                .accountNumber("AKS1234")
                .holderName("Akshara")
                .balance(5000.0)
                .status("ACTIVE")
                .createdAt(new Date())
                .build();

        when(accountRepository.findByAccountNumber("AKS1234")).thenReturn(Optional.of(account));

        Account result = accountService.getAccount("AKS1234");

        assertNotNull(result);
        assertEquals("Akshara", result.getHolderName());
    }

    // Test getAccount - not found
    @Test
    void testGetAccountNotFound() {
        when(accountRepository.findByAccountNumber("AKS9999")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccount("AKS9999"));
    }

    // Test deposit - invalid amount
    @Test
    void testDepositInvalidAmount() {
        DepositRequest request = new DepositRequest();
        request.setAmount(0.0);

        assertThrows(InvalidAmountException.class,
                () -> accountService.deposit("AKS1234", request));
    }

    // Add more tests for withdraw, transfer, delete etc.
}
