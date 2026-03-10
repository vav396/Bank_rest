package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardEncryption;
import com.example.bankcards.util.CardUtil;
import com.example.bankcards.exception.CardNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardEncryption cardEncryption;
    private final UserRepository userRepository;

    // Создаёт новую карту
    @Transactional
    public CardDto createCard(CardDto cardDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        com.example.bankcards.entity.User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));

        Card card = toEntity(cardDto);
        card.setUser(currentUser);

        String encryptedNumber = cardEncryption.encrypt(card.getNumber());
        card.setNumber(encryptedNumber);
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());

        Card savedCard = cardRepository.save(card);
        log.info("Card created for user: {}", currentUser.getId());

        return toDto(savedCard);
    }


    // Получает карту по ID
    @Transactional(readOnly = true)
    public CardDto getCardById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + id));

        card.setNumber(cardEncryption.decrypt(card.getNumber()));
        return toDto(card);
    }


    // Получает все карты пользователя с пагинацией
    @Transactional(readOnly = true)
    public Page<CardDto> getCardsByUserId(Long userId, Pageable pageable) {
        Page<Card> cardsPage = cardRepository.findByUserId(userId, pageable);

        return cardsPage.map(card -> {
            card.setNumber(cardEncryption.decrypt(card.getNumber()));
            return toDto(card);
        });
    }

    // Получает все карты системы с пагинацией
    @Transactional(readOnly = true)
    public Page<CardDto> getAllCards(Pageable pageable) {
        Page<Card> cardsPage = cardRepository.findAll(pageable);

        return cardsPage.map(card -> {
            card.setNumber(cardEncryption.decrypt(card.getNumber()));
            return toDto(card);
        });
    }

    /**
     * Удаляет карту по ID (только ADMIN)
     */
    @Transactional
    public void deleteCard(Long id) {
        log.info("Deleting card with id: {}", id);

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + id));

        cardRepository.delete(card);
        log.info("Card deleted successfully: {}", id);
    }

    // Маскирует номер карты для безопасного отображения
    public String getMaskedCardNumber(Long id) {
        CardDto card = getCardById(id);
        return CardUtil.maskCardNumber(card.getNumber());
    }

    //  МАППИНГ

    private CardDto toDto(Card card) {
        CardDto dto = new CardDto();
        dto.setId(card.getId());
        dto.setNumber(card.getNumber());
        dto.setOwner(card.getOwner());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setStatus(card.getStatus() != null ? card.getStatus().name() : "ACTIVE");
        dto.setBalance(card.getBalance());
        dto.setCreatedAt(card.getCreatedAt());
        dto.setUpdatedAt(card.getUpdatedAt());

        if (card.getUser() != null) {
            Hibernate.initialize(card.getUser());
            UserDto userDto = new UserDto();
            userDto.setId(card.getUser().getId());
            userDto.setUsername(card.getUser().getUsername());
            userDto.setEmail(card.getUser().getEmail());
            userDto.setRole(card.getUser().getRole() != null
                    ? card.getUser().getRole().name()
                    : "USER");
            dto.setUser(userDto);
        }
        return dto;
    }

    private Card toEntity(CardDto dto) {
        Card card = new Card();
        card.setId(dto.getId());
        card.setNumber(dto.getNumber());
        card.setOwner(dto.getOwner());
        card.setExpiryDate(dto.getExpiryDate());
        card.setStatus(dto.getStatus() != null
                ? Card.CardStatus.valueOf(dto.getStatus())
                : Card.CardStatus.ACTIVE);
        card.setBalance(dto.getBalance());

        if (dto.getUser() != null && dto.getUser().getId() != null) {
            com.example.bankcards.entity.User user = new com.example.bankcards.entity.User();
            user.setId(dto.getUser().getId());
            card.setUser(user);
        }
        return card;
    }
}