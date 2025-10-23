package org.example.cardservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CardResponse {
    private Long accountNumber;
    private Long cardNumber;
    private LocalDate expiryDate;
    private String paymentNetwork;
}
