package org.example.cardservice.dto;

import lombok.Data;

@Data
public class AccountRequest {
    private Long cardNumber;
    private String email;
    private String pin;
    private String Currency;
}
