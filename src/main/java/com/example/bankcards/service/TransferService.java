package com.example.bankcards.service;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final CardRepository cardRepository;

    @Transactional
    public void transferMoney(TransferRequest request, Long currentUserId) {
        log.info("=== НАЧАЛО ПЕРЕВОДА ===");
        log.info("fromCardId: {}, toCardId: {}, amount: {}, userId: {}",
                request.getFromCardId(), request.getToCardId(), request.getAmount(), currentUserId);

        Card fromCard = cardRepository.findById(request.getFromCardId())
                .orElseThrow(() -> new CardNotFoundException(
                        "Карта отправителя не найдена: " + request.getFromCardId()));
        log.info(" Карта отправителя найдена: id={}", fromCard.getId());

        Card toCard = cardRepository.findById(request.getToCardId())
                .orElseThrow(() -> new CardNotFoundException(
                        "Карта получателя не найдена: " + request.getToCardId()));
        log.info(" Карта получателя найдена: id={}", toCard.getId());

        Hibernate.initialize(fromCard.getUser());
        Hibernate.initialize(toCard.getUser());
        log.info(" Пользователи загружены");

        if (!fromCard.getUser().getId().equals(currentUserId)) {
            throw new SecurityException("Карта отправителя не принадлежит пользователю");
        }
        if (!toCard.getUser().getId().equals(currentUserId)) {
            throw new SecurityException("Карта получателя не принадлежит пользователю");
        }

        if (fromCard.getId().equals(toCard.getId())) {
            throw new IllegalArgumentException("Нельзя перевести на ту же карту");
        }

        if (fromCard.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
        }
        log.info(" Все проверки пройдены");

        fromCard.setBalance(fromCard.getBalance().subtract(request.getAmount()));
        toCard.setBalance(toCard.getBalance().add(request.getAmount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        log.info("=== ПЕРЕВОД УСПЕШНО ЗАВЕРШЁН ===");
    }
}