package com.example.demo.service;

import com.example.demo.model.Transaction;
import com.example.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepo;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void testRecordDeposit() {
        // Arrange
        when(transactionRepo.save(any(Transaction.class))).thenReturn(new Transaction());

        // Act
        transactionService.recordDeposit("MAD1234", 500.0);

        // Assert (only verify call)
        verify(transactionRepo, times(1)).save(any(Transaction.class));
    }

    @Test
    void testRecordWithdraw() {
        when(transactionRepo.save(any(Transaction.class))).thenReturn(new Transaction());

        transactionService.recordWithdraw("AKS1234", 300.0);

        verify(transactionRepo, times(1)).save(any(Transaction.class));
    }

    @Test
    void testRecordTransfer() {
        when(transactionRepo.save(any(Transaction.class))).thenReturn(new Transaction());

        transactionService.recordTransfer("SRC001", "DEST001", 200.0);

        verify(transactionRepo, times(1)).save(any(Transaction.class));
    }
}
