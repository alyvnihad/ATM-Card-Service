package org.example.cardservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.cardservice.dto.AccountRequest;
import org.example.cardservice.model.Card;
import org.example.cardservice.service.CardService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping("register")
    public void register(@RequestBody AccountRequest request){
        cardService.register(request.getPinHash(), request.getCurrency());
    }

    @GetMapping("read-card")
    public Optional<Card> getCard(@RequestBody AccountRequest request){
        return cardService.getCard(request.getCardNumber());
    }

    @GetMapping("pin-check")
    public boolean pinHashCheck(@RequestBody AccountRequest request){
        return cardService.pinHashCheck(request.getCardNumber(), request.getPinHash());
    }
}
