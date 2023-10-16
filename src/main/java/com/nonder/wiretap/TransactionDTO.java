package com.nonder.wiretap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String sender;
    private String receiver;
    private double amount;
    private String fraud;
}
