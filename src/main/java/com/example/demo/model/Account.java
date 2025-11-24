package com.example.demo.model;


import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "accountsdb")
public class Account {

    @Id
    private String id;               // MongoDB _id

    private String accountNumber;    // e.g., JOH1234
    private String holderName;
    private Double balance;
    private String status;           // ACTIVE or INACTIVE
    private Date createdAt;
}
