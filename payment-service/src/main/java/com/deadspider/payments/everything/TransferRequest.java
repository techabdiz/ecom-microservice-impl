package com.deadspider.payments.everything;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    private String accno;
    private Double amount;
    private String narration;
    private String type = "DEBIT";
}
