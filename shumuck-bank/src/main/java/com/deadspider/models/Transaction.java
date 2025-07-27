package com.deadspider.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Builder
@Table(name = "tbl_shmuck_transactions")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    private TransactionType type;
    private String refnum;
    private String narration;

    private Double amount;

    private Instant timestamp;

    @ManyToOne
    @JsonIgnore
    @PrimaryKeyJoinColumn
    private Account account;


}
