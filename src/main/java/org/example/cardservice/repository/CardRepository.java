package org.example.cardservice.repository;

import org.example.cardservice.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CardRepository extends JpaRepository<Card,Long> {
}
