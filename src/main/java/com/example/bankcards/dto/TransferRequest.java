package com.example.bankcards.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotNull(message = "ID карты отправителя не может быть пустым")
    private Long fromCardId;

    @NotNull(message = "ID карты получателя не может быть пустым")
    private Long toCardId;

    @NotNull(message = "Сумма перевода не может быть пустой")
    @DecimalMin(value = "0.01", inclusive = true, message = "Сумма перевода должна быть больше 0")
    private BigDecimal amount;
}