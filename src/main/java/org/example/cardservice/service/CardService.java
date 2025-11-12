package org.example.cardservice.service;

import lombok.RequiredArgsConstructor;
import org.example.cardservice.dto.AccountRequest;
import org.example.cardservice.dto.AccountResponse;
import org.example.cardservice.dto.CardResponse;
import org.example.cardservice.exception.ConflictException;
import org.example.cardservice.model.Card;
import org.example.cardservice.model.CardStatus;
import org.example.cardservice.model.PaymentNetwork;
import org.example.cardservice.repository.CardRepository;
import org.example.cardservice.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

// Handles card creation, pin checks, and admin block/unblock actions
@Service
@RequiredArgsConstructor
public class CardService {

    private final SecurityFilter filter;
    private final CardRepository cardRepository;
    private final RestTemplate restTemplate;

    @Value("${account.service.url}")
    private String accountUrl;

    // Create a new card and register related account
    public CardResponse register(AccountRequest request) {

        // Encode pin
        String Pin = filter.passwordEncoder(request.getPin());
        // Generate new card number
        Long cardNumber = filter.cardNumber();
        // Generate new Payment Network (VISA or Master)
        PaymentNetwork paymentNetwork = filter.generatedPaymentNetwork();
        // Create a new card
        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setExpiryDate(LocalDate.now().plusYears(3));
        card.setPinHash(Pin);
        card.setFailedPin(0);
        card.setCardStatus(CardStatus.Unblock);
        card.setPaymentNetwork(paymentNetwork);
        cardRepository.save(card);

        // Prepare Account request
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCardNumber(cardNumber);
        accountRequest.setCurrency(request.getCurrency());
        accountRequest.setEmail(request.getEmail());

        // Prepare card response for Auth Service
        CardResponse cardResponse = new CardResponse();
        cardResponse.setCardNumber(cardNumber);
        cardResponse.setExpiryDate(card.getExpiryDate());
        cardResponse.setPaymentNetwork(String.valueOf(card.getPaymentNetwork()));

        // Send Account Service call
        ResponseEntity<AccountResponse> accountResponse = restTemplate.postForEntity(accountUrl + "/register", accountRequest, AccountResponse.class);
        if (accountResponse.getBody() != null) {
            cardResponse.setAccountNumber(accountResponse.getBody().getAccountNumber());
        }

        return cardResponse;
    }

    // Get card info
    public Optional<Card> getCard(Long number) {
        Card card = cardRepository.findByCardNumber(number);
        return cardRepository.findByIdAndCardNumber(card.getId(), card.getCardNumber());
    }

    // Check card pin (auto-block after 3 failed attempts)
    public boolean pinHashCheck(Long cardNumber, String pin) {
        Card card = cardRepository.findByCardNumber(cardNumber);

        if (card.getCardStatus() == CardStatus.Block) {
            return false;
        }

        boolean decoder = filter.passwordDecoder(card.getPinHash(), pin);
        if (!decoder) {
            card.setFailedPin(card.getFailedPin() + 1);

            // Card is blocked if pin fails three times
            if (card.getFailedPin() >= 3) {
                card.setCardStatus(CardStatus.Block);
            }
            cardRepository.save(card);
            return false;
        }

        // Reset failed pin count
        card.setFailedPin(0);
        cardRepository.save(card);
        return true;
    }

    // Block card (admin only)
    public void cardBlock(Long number) {
        try {
            Card card = cardRepository.findByCardNumber(number);
            card.setCardStatus(CardStatus.Block);
            cardRepository.save(card);
        } catch (Exception e) {
            throw new ConflictException("Card Block Error");
        }
    }

    // Unblock card (admin only)
    public void cardUnBlock(Long number) {
        try {
            Card card = cardRepository.findByCardNumber(number);
            card.setCardStatus(CardStatus.Unblock);
            card.setFailedPin(0);
            cardRepository.save(card);
        } catch (Exception e) {
            throw new ConflictException("Card UnBlock Error");
        }
    }
}
