package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.demo.exception.*;
import com.example.demo.model.Account;
import com.example.demo.model.DTOs.*;
import com.example.demo.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    // Generate account number: initials + random 4 digits
    private String generateAccountNumber(String name) {
        String initials = name.substring(0, 3).toUpperCase();
        int random = new Random().nextInt(9000) + 1000;
        return initials + random;
    }

    @Override
    public Account createAccount(CreateAccountRequest request) {
        log.info("Creating account for holder: {}", request.getHolderName());

        String accNumber = generateAccountNumber(request.getHolderName());

        Account acc = Account.builder()
                .accountNumber(accNumber)
                .holderName(request.getHolderName())
                .balance(0.0)
                .status("ACTIVE")
                .createdAt(new Date())
                .build();

        Account saved = accountRepository.save(acc);

        log.info("Account created successfully with accountNumber: {}", saved.getAccountNumber());
        return saved;
    }

    @Override
    public Account getAccount(String accountNumber) {
        log.info("Fetching account with accountNumber: {}", accountNumber);
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.error("Account not found: {}", accountNumber);
                    return new AccountNotFoundException("Account not found: " + accountNumber);
                });

        log.info("Account retrieved successfully: {}", acc.getAccountNumber());
        return acc;
    }

    @Override
    public Account deposit(String accountNumber, DepositRequest request) {
        log.info("Depositing amount {} into account: {}", request.getAmount(), accountNumber);

        if (request.getAmount() <= 0) {
            log.error("Invalid deposit amount: {}", request.getAmount());
            throw new InvalidAmountException("Amount must be greater than 0");
        }

        Account acc = getAccount(accountNumber);
        acc.setBalance(acc.getBalance() + request.getAmount());
        accountRepository.save(acc);

        transactionService.recordDeposit(accountNumber, request.getAmount());

        log.info("Deposit successful. New balance: {}", acc.getBalance());
        return acc;
    }

    @Override
    public Account withdraw(String accountNumber, WithdrawRequest request) {
        log.info("Withdrawing amount {} from account: {}", request.getAmount(), accountNumber);

        if (request.getAmount() <= 0) {
            log.error("Invalid withdraw amount: {}", request.getAmount());
            throw new InvalidAmountException("Amount must be > 0");
        }

        Account acc = getAccount(accountNumber);

        if (acc.getBalance() < request.getAmount()) {
            log.error("Insufficient balance. Current balance: {}, Withdraw requested: {}", acc.getBalance(), request.getAmount());
            throw new InsufficientBalanceException("Balance not sufficient");
        }

        acc.setBalance(acc.getBalance() - request.getAmount());
        accountRepository.save(acc);

        transactionService.recordWithdraw(accountNumber, request.getAmount());

        log.info("Withdrawal successful. New balance: {}", acc.getBalance());
        return acc;
    }

    @Override
    public String transfer(TransferRequest request) {
        log.info("Transferring amount {} from {} to {}", request.getAmount(), request.getSourceAccount(), request.getDestinationAccount());

        if (request.getAmount() <= 0) {
            log.error("Invalid transfer amount: {}", request.getAmount());
            throw new InvalidAmountException("Amount must be > 0");
        }

        Account source = getAccount(request.getSourceAccount());
        Account dest = getAccount(request.getDestinationAccount());

        if (source.getBalance() < request.getAmount()) {
            log.error("Insufficient funds in source account: {}", source.getAccountNumber());
            throw new InsufficientBalanceException("Insufficient funds");
        }

        source.setBalance(source.getBalance() - request.getAmount());
        dest.setBalance(dest.getBalance() + request.getAmount());

        accountRepository.save(source);
        accountRepository.save(dest);

        transactionService.recordTransfer(
                request.getSourceAccount(),
                request.getDestinationAccount(),
                request.getAmount()
        );

        log.info("Transfer successful. Source balance: {}, Destination balance: {}", source.getBalance(), dest.getBalance());
        return "Transfer successful";
    }

    @Override
    public void deleteAccount(String accountNumber) {
        log.info("Deleting account: {}", accountNumber);

        Account acc = getAccount(accountNumber);
        accountRepository.delete(acc);

        log.info("Account deleted successfully: {}", accountNumber);
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        log.info("Fetched all accounts. Total count: {}", accounts.size());
        return accounts;
    }
}












//package com.example.demo.service;
//
//
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
//import org.springframework.stereotype.Service;
//
//import com.example.demo.exception.*;
//import com.example.demo.model.Account;
//import com.example.demo.model.DTOs.*;
//import com.example.demo.repository.AccountRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class AccountServiceImpl implements AccountService {
//
//    private final AccountRepository accountRepository;
//    private final TransactionService transactionService;
//
//    // Generate account number: initials + random 4 digits
//    private String generateAccountNumber(String name) {
//        String initials = name.substring(0, 3).toUpperCase();
//        int random = new Random().nextInt(9000) + 1000;
//        return initials + random;
//    }
//
//    @Override
//    public Account createAccount(CreateAccountRequest request) {
//        String accNumber = generateAccountNumber(request.getHolderName());
//
//        Account acc = Account.builder()
//                .accountNumber(accNumber)
//                .holderName(request.getHolderName())
//                .balance(0.0)
//                .status("ACTIVE")
//                .createdAt(new Date())
//                .build();
//
//        return accountRepository.save(acc);
//    }
//
//    @Override
//    public Account getAccount(String accountNumber) {
//        return accountRepository.findByAccountNumber(accountNumber)
//                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
//    }
//
//    @Override
//    public Account deposit(String accountNumber, DepositRequest request) {
//        if (request.getAmount() <= 0)
//            throw new InvalidAmountException("Amount must be greater than 0");
//
//        Account acc = getAccount(accountNumber);
//
//        acc.setBalance(acc.getBalance() + request.getAmount());
//        accountRepository.save(acc);
//
//        transactionService.recordDeposit(accountNumber, request.getAmount());
//
//        return acc;
//    }
//
//    @Override
//    public Account withdraw(String accountNumber, WithdrawRequest request) {
//        if (request.getAmount() <= 0)
//            throw new InvalidAmountException("Amount must be > 0");
//
//        Account acc = getAccount(accountNumber);
//
//        if (acc.getBalance() < request.getAmount())
//            throw new InsufficientBalanceException("Balance not sufficient");
//
//        acc.setBalance(acc.getBalance() - request.getAmount());
//        accountRepository.save(acc);
//
//        transactionService.recordWithdraw(accountNumber, request.getAmount());
//
//        return acc;
//    }
//
//    @Override
//    public String transfer(TransferRequest request) {
//        if (request.getAmount() <= 0)
//            throw new InvalidAmountException("Amount must be > 0");
//
//        Account source = getAccount(request.getSourceAccount());
//        Account dest = getAccount(request.getDestinationAccount());
//
//        if (source.getBalance() < request.getAmount())
//            throw new InsufficientBalanceException("Insufficient funds");
//
//        source.setBalance(source.getBalance() - request.getAmount());
//        dest.setBalance(dest.getBalance() + request.getAmount());
//
//        accountRepository.save(source);
//        accountRepository.save(dest);
//
//        transactionService.recordTransfer(
//                request.getSourceAccount(),
//                request.getDestinationAccount(),
//                request.getAmount()
//        );
//
//        return "Transfer successful";
//    }
//
//    @Override
//    public void deleteAccount(String accountNumber) {
//        Account acc = getAccount(accountNumber);
//        accountRepository.delete(acc);
//    }
//    @Override
//    public List<Account> getAllAccounts() {
//        return accountRepository.findAll(); // JpaRepository provides findAll()
//    }
//
//    
//}
