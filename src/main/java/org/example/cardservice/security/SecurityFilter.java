package org.example.cardservice.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.RequiredArgsConstructor;
import org.example.cardservice.model.PaymentNetwork;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;


@Component
@RequiredArgsConstructor
public class SecurityFilter {

    private static final AtomicLong counter = new AtomicLong(0);
    private final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    public String passwordEncoder(String pin) {
        return argon2.hash(4, 65536, 3, pin);
    }

    public boolean passwordDecoder(String dbPinHashed, String enteredPin) {
        return argon2.verify(dbPinHashed, enteredPin);
    }

    public Long cardNumber() {
        long base = 12341234L * 10000L * 10000L;
        return  base + generatedValue();
    }

    protected Long generatedValue() {
        return System.currentTimeMillis() / 100000 + counter.getAndIncrement();
    }

    public PaymentNetwork generatedPaymentNetwork() {
        PaymentNetwork[] values = PaymentNetwork.values();
        Random random = new Random();
        int index = random.nextInt(values.length);
        return values[index];
    }
}
