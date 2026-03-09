package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    private Long id;

    @NotBlank(message = "Номер карты не может быть пустым")
    @Size(min = 16, max = 19, message = "Номер карты должен содержать от 16 до 19 цифр")
    @Pattern(regexp = "^\\d+$", message = "Номер карты должен содержать только цифры")
    private String number;

    @NotBlank(message = "Имя владельца не может быть пустым")
    @Size(min = 2, max = 100, message = "Имя владельца должно быть от 2 до 100 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s\\-']+$", message = "Имя содержит недопустимые символы")
    private String owner;

    @NotNull(message = "Срок действия не может быть пустым")
    @Future(message = "Срок действия должен быть в будущем")
    private LocalDate expiryDate;

    @Pattern(regexp = "^(ACTIVE|BLOCKED|EXPIRED|CLOSED)$", message = "Недопустимый статус карты")
    private String status;

    @NotNull(message = "Баланс не может быть пустым")
    @DecimalMin(value = "0.0", inclusive = true, message = "Баланс не может быть отрицательным")
    private BigDecimal balance;

    private UserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}