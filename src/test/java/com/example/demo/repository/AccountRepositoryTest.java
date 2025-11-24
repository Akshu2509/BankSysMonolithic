//package com.example.demo.repository;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.Optional;
//
//import com.example.demo.model.Account;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
////import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//
//@DataMongoTest
//class AccountRepositoryTest {
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Test
//    void testSaveAndFindAccount() {
//        Account acc = new Account();
//        acc.setHolderName("Test User");
//        acc.setAccountNumber("TST1234");
//        acc.setBalance(1000.0);
//        acc.setStatus("ACTIVE");
//
//        Account saved = accountRepository.save(acc);
//
//        Optional<Account> found = accountRepository.findByAccountNumber("TST1234");
//
//        assertTrue(found.isPresent());
//        assertEquals("Test User", found.get().getHolderName());
//
//        // Cleanup
//        accountRepository.delete(saved);
//    }
//}
