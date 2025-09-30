package org.example.cardservice.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.RequiredArgsConstructor;
import org.example.cardservice.model.PaymentNetwork;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
@RequiredArgsConstructor
public class SecurityFilter {

    private final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    public String passwordEncoder(String pin) {
       return argon2.hash(4, 65536, 3, pin);
    }

    public Long cardNumber(){
        Long value = generatedValue();
        return 12341234 + value;
    }

    protected Long generatedValue(){
        return System.currentTimeMillis()/100000;
    }

    public PaymentNetwork generatedPaymentNetwork(){
        PaymentNetwork[] values = PaymentNetwork.values();
        Random random = new Random();
        int index = random.nextInt(values.length);
        return values[index];
    }
}
