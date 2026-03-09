package com.example.bankcards.service;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock private CardRepository cardRepository;
    @InjectMocks private TransferService transferService;

    private Card fromCard;
    private Card toCard;
    private User owner;
    private TransferRequest request;

    @BeforeEach
    void setUp() {
        // Создаём владельца
        owner = new User();
        owner.setId(1L);
        owner.setUsername("testuser");

        // Карта отправителя
        fromCard = new Card();
        fromCard.setId(1L);
        fromCard.setNumber("4111111111111111");
        fromCard.setOwner("Test User");
        fromCard.setExpiryDate(LocalDate.of(2025, 12, 1));
        fromCard.setStatus(Card.CardStatus.ACTIVE);
        fromCard.setBalance(BigDecimal.valueOf(1000));
        fromCard.setUser(owner);

        // Карта получателя
        toCard = new Card();
        toCard.setId(2L);
        toCard.setNumber("5555555555554444");
        toCard.setOwner("Test User");
        toCard.setExpiryDate(LocalDate.of(2025, 12, 1));
        toCard.setStatus(Card.CardStatus.ACTIVE);
        toCard.setBalance(BigDecimal.valueOf(500));
        toCard.setUser(owner);

        // Запрос на перевод
        request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(100));
    }

    @Test
    void testTransferMoney_Success() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        assertDoesNotThrow(() ->
                transferService.transferMoney(request, 1L)
        );

        assertEquals(BigDecimal.valueOf(900), fromCard.getBalance());
        assertEquals(BigDecimal.valueOf(600), toCard.getBalance());
        verify(cardRepository).save(fromCard);
        verify(cardRepository).save(toCard);
    }

    @Test
    void testTransferMoney_FromCardNotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () ->
                transferService.transferMoney(request, 1L)
        );
    }

    @Test
    void testTransferMoney_ToCardNotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () ->
                transferService.transferMoney(request, 1L)
        );
    }

    @Test
    void testTransferMoney_InsufficientFunds() {
        request.setAmount(BigDecimal.valueOf(5000));
        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        assertThrows(InsufficientFundsException.class, () ->
                transferService.transferMoney(request, 1L)
        );
    }

    @Test
    void testTransferMoney_SameCard() {
        request.setToCardId(1L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));

        assertThrows(IllegalArgumentException.class, () ->
                transferService.transferMoney(request, 1L)
        );
    }

    @Test
    void testTransferMoney_WrongOwner() {
        User otherUser = new User();
        otherUser.setId(99L);
        toCard.setUser(otherUser);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        assertThrows(SecurityException.class, () ->
                transferService.transferMoney(request, 1L)
        );
    }
}