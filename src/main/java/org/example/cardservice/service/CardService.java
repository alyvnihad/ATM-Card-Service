package org.example.cardservice.service;

import lombok.RequiredArgsConstructor;
import org.example.cardservice.dto.AccountRequest;
import org.example.cardservice.dto.AccountResponse;
import org.example.cardservice.model.Card;
import org.example.cardservice.model.CardStatus;
import org.example.cardservice.model.PaymentNetwork;
import org.example.cardservice.repository.CardRepository;
import org.example.cardservice.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CardService {

    private final SecurityFilter filter;
    private final CardRepository cardRepository;
    private final RestTemplate restTemplate;

    @Value("${account.service.url}")
    private String accountUrl;

    public AccountResponse register(String pin, String currency) {
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

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setCardNumber(cardNumber);

        restTemplate.postForEntity(accountUrl + "/register", accountRequest, Void.class);
        return accountResponse;
    }

    public Optional<Card> getCard(Long number) {
        Card card = cardRepository.findByCardNumber(number);
        return cardRepository.findByIdAndCardNumber(card.getId(), card.getCardNumber());
    }

    public boolean pinHashCheck(Long cardNumber, String pin) {
        Card card = cardRepository.findByCardNumber(cardNumber);

        if (card.getCardStatus() == CardStatus.Block) {
            return false;
        }

        boolean decoder = filter.passwordDecoder(card.getPinHash(), pin);
        if (!decoder) {
            card.setFailedPin(card.getFailedPin() + 1);

            if (card.getFailedPin() >= 3) {
                card.setCardStatus(CardStatus.Block);
            }
            cardRepository.save(card);
            return false;
        }

        card.setFailedPin(0);
        cardRepository.save(card);
        return true;
    }

    public void cardBlock(Long number) {
        try {
            Card card = cardRepository.findByCardNumber(number);
            card.setCardStatus(CardStatus.Block);
            cardRepository.save(card);
        } catch (Exception e) {
            throw new RuntimeException("Error line 83");
        }
    }

    public void cardUnBlock(Long number) {
        try {
            Card card = cardRepository.findByCardNumber(number);
            card.setCardStatus(CardStatus.Unblock);
            card.setFailedPin(0);
            cardRepository.save(card);
        } catch (Exception e) {
            throw new RuntimeException("Error line 94");
        }
    }
}
