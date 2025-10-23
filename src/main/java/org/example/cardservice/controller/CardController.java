package org.example.cardservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.cardservice.dto.AccountRequest;
import org.example.cardservice.dto.CardResponse;
import org.example.cardservice.model.Card;
import org.example.cardservice.service.CardService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping("/register")
    public CardResponse register(@RequestBody AccountRequest request) {
        return cardService.register(request);
    }

    @GetMapping("/read-card")
    public Optional<Card> getCard(@RequestBody AccountRequest request) {
        return cardService.getCard(request.getCardNumber());
    }

    @PostMapping("/pin-check")
    public boolean pinHashCheck(@RequestBody AccountRequest request) {
        return cardService.pinHashCheck(request.getCardNumber(), request.getPin());
    }

    @PostMapping("/block")
    public void block(@RequestBody AccountRequest request) {
        cardService.cardBlock(request.getCardNumber());
    }

    @PostMapping("/unblock")
    public void unBlock(@RequestBody AccountRequest request) {
        cardService.cardUnBlock(request.getCardNumber());
    }
}
