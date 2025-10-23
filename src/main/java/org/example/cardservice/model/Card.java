package org.example.cardservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cardNumber;
    private LocalDate expiryDate;
    private String pinHash;
    private int failedPin;
    @Enumerated(value = EnumType.STRING)
    private CardStatus cardStatus;
    @Enumerated(value = EnumType.STRING)
    private PaymentNetwork paymentNetwork;
}
