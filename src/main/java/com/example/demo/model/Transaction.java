package com.example.demo.model;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "transactionsdb")
public class Transaction {

    @Id
    private String id;                // MongoDB _id

    private String transactionId;     // custom ID like TXN-20251107-001
    private String type;              // DEPOSIT, WITHDRAW, TRANSFER
    private Double amount;
    private Date timestamp;
    private String status;            // SUCCESS or FAILED

    private String sourceAccount;     // For transfers
    private String destinationAccount;
}
