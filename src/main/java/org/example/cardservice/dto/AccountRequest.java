package org.example.cardservice.dto;

import lombok.Data;

@Data
public class AccountRequest {
    private String pinHash;
    private String Currency;
}
