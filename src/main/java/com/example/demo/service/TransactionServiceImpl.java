package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.model.Transaction;
import com.example.demo.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private String generateTxnId() {
        String txnId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        logger.debug("Generated Transaction ID: {}", txnId);
        return txnId;
    }

    @Override
    public void recordDeposit(String accountNumber, double amount) {
        Transaction txn = Transaction.builder()
                .transactionId(generateTxnId())
                .type("DEPOSIT")
                .amount(amount)
                .timestamp(new Date())
                .status("SUCCESS")
                .destinationAccount(accountNumber)
                .build();

        transactionRepository.save(txn);
        logger.info("Deposit recorded: {} to account {}", amount, accountNumber);
    }

    @Override
    public void recordWithdraw(String accountNumber, double amount) {
        Transaction txn = Transaction.builder()
                .transactionId(generateTxnId())
                .type("WITHDRAW")
                .amount(amount)
                .timestamp(new Date())
                .status("SUCCESS")
                .sourceAccount(accountNumber)
                .build();

        transactionRepository.save(txn);
        logger.info("Withdrawal recorded: {} from account {}", amount, accountNumber);
    }

    @Override
    public void recordTransfer(String source, String dest, double amount) {
        Transaction txn = Transaction.builder()
                .transactionId(generateTxnId())
                .type("TRANSFER")
                .amount(amount)
                .timestamp(new Date())
                .status("SUCCESS")
                .sourceAccount(source)
                .destinationAccount(dest)
                .build();

        transactionRepository.save(txn);
        logger.info("Transfer recorded: {} from {} to {}", amount, source, dest);
    }

    @Override
    public List<Transaction> getTransactions(String accountNumber) {
        List<Transaction> transactions = transactionRepository.findBySourceAccountOrDestinationAccount(accountNumber, accountNumber);
        logger.info("Retrieved {} transactions for account {}", transactions.size(), accountNumber);
        return transactions;
    }
}
