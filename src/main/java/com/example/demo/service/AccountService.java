package com.example.demo.service;
import java.util.List;

import com.example.demo.model.Account;
import com.example.demo.model.DTOs.*;

public interface AccountService {

    Account createAccount(CreateAccountRequest request);

    Account getAccount(String accountNumber);
    List<Account> getAllAccounts();
    Account deposit(String accountNumber, DepositRequest request);

    Account withdraw(String accountNumber, WithdrawRequest request);

    String transfer(TransferRequest request); 

    void deleteAccount(String accountNumber);
}