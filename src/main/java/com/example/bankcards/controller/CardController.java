package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    /**
     * POST /api/cards - Создать новую карту
     */
    @PostMapping
    public ResponseEntity<CardDto> createCard(@RequestBody @Valid CardDto cardDto) {
        log.info("Creating new card for user");
        CardDto createdCard = cardService.createCard(cardDto);
        return ResponseEntity.ok(createdCard);
    }

    /**
     * GET /api/cards/{id} - Получить карту по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCardById(@PathVariable Long id) {
        log.info("Getting card by id: {}", id);
        CardDto card = cardService.getCardById(id);
        return ResponseEntity.ok(card);
    }

    /**
     * GET /api/cards/user/{userId} - Получить все карты пользователя
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CardDto>> getCardsByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Getting cards for user: {} with pagination", userId);
        Page<CardDto> cards = cardService.getCardsByUserId(userId, pageable);
        return ResponseEntity.ok(cards);
    }

    /**
     * GET /api/cards - Получить все карты
     */
    @GetMapping
    public ResponseEntity<Page<CardDto>> getAllCards(
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Getting all cards with pagination");
        Page<CardDto> cards = cardService.getAllCards(pageable);
        return ResponseEntity.ok(cards);
    }

    /**
     * GET /api/cards/{id}/masked - Получить маскированный номер карты
     */
    @GetMapping("/{id}/masked")
    public ResponseEntity<String> getMaskedCardNumber(@PathVariable Long id) {
        log.info("Getting masked card number for id: {}", id);
        String maskedNumber = cardService.getMaskedCardNumber(id);
        return ResponseEntity.ok(maskedNumber);
    }

    /**
     * DELETE /api/cards/{id} - Удалить карту (только ADMIN)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        log.info("Deleting card with id: {}", id);
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT /api/cards/{id}/block - Блокировать карту (только ADMIN)
     */
    @PutMapping("/{id}/block")
    public ResponseEntity<CardDto> blockCard(@PathVariable Long id) {
        log.info("Blocking card with id: {}", id);
        CardDto blockedCard = cardService.blockCard(id);
        return ResponseEntity.ok(blockedCard);
    }

    /**
     * PUT /api/cards/{id}/activate - Активировать карту (только ADMIN)
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<CardDto> activateCard(@PathVariable Long id) {
        log.info("Activating card with id: {}", id);
        CardDto activatedCard = cardService.activateCard(id);
        return ResponseEntity.ok(activatedCard);
    }

    /**
     * POST /api/cards/me/{id}/block-request - Запросить блокировку карты (пользователь)
     */
    @PostMapping("/me/{id}/block-request")
    public ResponseEntity<CardDto> requestBlockCard(@PathVariable Long id) {
        log.info("Requesting block for card with id: {}", id);
        CardDto blockedCard = cardService.requestBlockCard(id);
        return ResponseEntity.ok(blockedCard);
    }

    /**
     * GET /api/cards/me/{id}/balance - Получить баланс карты
     */
    @GetMapping("/me/{id}/balance")
    public ResponseEntity<java.math.BigDecimal> getCardBalance(@PathVariable Long id) {
        log.info("Getting balance for card with id: {}", id);
        java.math.BigDecimal balance = cardService.getCardBalance(id);
        return ResponseEntity.ok(balance);
    }

    /**
     * GET /api/cards/me - Получить все карты текущего пользователя
     */
    @GetMapping("/me")
    public ResponseEntity<Page<CardDto>> getMyCards(
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Getting cards for current user with pagination");
        Page<CardDto> cards = cardService.getMyCards(pageable);
        return ResponseEntity.ok(cards);
    }
}