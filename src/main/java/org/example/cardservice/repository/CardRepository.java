package org.example.cardservice.repository;

import org.example.cardservice.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CardRepository extends JpaRepository<Card,Long> {
    Optional<Card> findByIdAndCardNumber(Long id, Long cardNumber);
    Card findByCardNumber(Long cardNumber);
}
