package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.CardEncryption;
import com.example.bankcards.exception.CardNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock private CardRepository cardRepository;
    @Mock private CardEncryption cardEncryption;
    @InjectMocks private CardService cardService;

    private Card testCard;
    private CardDto testCardDto;

    @BeforeEach
    void setUp() {
        // Сущность Card
        testCard = new Card();
        testCard.setId(1L);
        testCard.setNumber("4111111111111111");
        testCard.setOwner("John Doe");
        testCard.setExpiryDate(LocalDate.of(2025, 12, 1));
        testCard.setStatus(Card.CardStatus.ACTIVE);
        testCard.setBalance(BigDecimal.valueOf(1000));
        testCard.setCreatedAt(LocalDateTime.now());

        // DTO
        testCardDto = new CardDto();
        testCardDto.setId(1L);
        testCardDto.setNumber("4111111111111111");
        testCardDto.setOwner("John Doe");
        testCardDto.setExpiryDate(LocalDate.of(2025, 12, 1));
        testCardDto.setStatus("ACTIVE");
        testCardDto.setBalance(BigDecimal.valueOf(1000));

        // UserDto для связи
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        testCardDto.setUser(userDto);
    }

    @Test
    void testCreateCard() {
        when(cardEncryption.encrypt(any(String.class))).thenReturn("encrypted_4111111111111111");
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        CardDto result = cardService.createCard(testCardDto);

        assertNotNull(result);
        assertEquals(testCardDto.getId(), result.getId());
        verify(cardEncryption).encrypt(testCardDto.getNumber());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void testGetCardById_Success() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardEncryption.decrypt(any(String.class))).thenReturn("4111111111111111");

        CardDto result = cardService.getCardById(1L);

        assertNotNull(result);
        assertEquals("4111111111111111", result.getNumber());
        verify(cardRepository).findById(1L);
    }

    @Test
    void testGetCardById_NotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.getCardById(1L));
    }

    @Test
    void testGetMaskedCardNumber() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardEncryption.decrypt(any(String.class))).thenReturn("4111111111111111");

        String masked = cardService.getMaskedCardNumber(1L);

        assertNotNull(masked);
        assertTrue(masked.contains("****"));
    }
}