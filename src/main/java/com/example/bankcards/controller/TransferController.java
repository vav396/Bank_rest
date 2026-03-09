package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.security.UserDetailsImpl;
import com.example.bankcards.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transferMoney(
            @RequestBody @Valid TransferRequest request,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        log.info("Запрос на перевод: с карты {} на карту {}, сумма: {}",
                request.getFromCardId(), request.getToCardId(), request.getAmount());

        transferService.transferMoney(request, currentUser.getId());

        log.info("Перевод успешно выполнен для пользователя: {}", currentUser.getId());
        return ResponseEntity.ok().build();
    }
}