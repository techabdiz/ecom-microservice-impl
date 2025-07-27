package com.deadspider.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "tbl_shmuck_accounts")
@NoArgsConstructor
@AllArgsConstructor
public class Account {


    @Id
    @GeneratedValue
    private Long id;

    private String accno;
    private String holderName;
    private String holderAddress;
    private String holderEmail;

    private Double currentBalance;
    private Instant creationDate;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

}
