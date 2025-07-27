package com.deadspider.models.dtos;

import com.deadspider.models.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private String accno;
    private Double amount;
    private String narration;
    private TransactionType type;
}
