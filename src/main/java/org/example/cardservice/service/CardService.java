package org.example.cardservice.service;

import lombok.RequiredArgsConstructor;
import org.example.cardservice.dto.AccountRequest;
import org.example.cardservice.model.Card;
import org.example.cardservice.model.CardStatus;
import org.example.cardservice.model.PaymentNetwork;
import org.example.cardservice.repository.CardRepository;
import org.example.cardservice.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class CardService {

    private final SecurityFilter filter;
    private final CardRepository cardRepository;
    private final RestTemplate restTemplate;

    @Value("${account.service.url}")
    private String accountUrl;

    public void register(String pin, String currency) {
        String Pin = filter.passwordEncoder(pin);
        Long cardNumber = filter.cardNumber();
        PaymentNetwork paymentNetwork = filter.generatedPaymentNetwork();
        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setExpiryDate(LocalDate.now().plusYears(3));
        card.setPinHash(Pin);
        card.setFailedPin(0);
        card.setCardStatus(CardStatus.Unblock);
        card.setPaymentNetwork(paymentNetwork);
        cardRepository.save(card);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCardNumber(cardNumber);
        accountRequest.setCurrency(currency);

        restTemplate.postForEntity(accountUrl + "/register", accountRequest, Void.class);
    }
}
