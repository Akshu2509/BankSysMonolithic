package com.example.demo.service;

import java.util.List;
import com.example.demo.model.Transaction;

public interface TransactionService {

    void recordDeposit(String accountNumber, double amount);

    void recordWithdraw(String accountNumber, double amount);

    void recordTransfer(String sourceAccount, String destAccount, double amount);

    List<Transaction> getTransactions(String accountNumber);
}
