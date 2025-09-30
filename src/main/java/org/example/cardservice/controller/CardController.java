package org.example.cardservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.cardservice.dto.AccountRequest;
import org.example.cardservice.service.CardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping("register")
    public void register(@RequestBody AccountRequest request){
        cardService.register(request.getPinHash(), request.getCurrency());
    }
}
