package org.example.cardservice.dto;

import lombok.Data;

@Data
public class AccountRequest {
    private Long cardNumber;
    private String pinHash;
    private String Currency;
}
