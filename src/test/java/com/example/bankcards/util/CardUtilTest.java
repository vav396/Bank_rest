package com.example.bankcards.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardUtilTest {

    @Test
    void maskCardNumber_Success() {
        // Arrange
        String cardNumber = "4532015112830366";

        // Act
        String masked = CardUtil.maskCardNumber(cardNumber);

        // Assert - формат с пробелами, как в CardUtil
        assertEquals("**** **** **** 0366", masked);
    }

    @Test
    void maskCardNumber_ShortNumber() {
        // Arrange
        String cardNumber = "123";

        // Act
        String masked = CardUtil.maskCardNumber(cardNumber);

        // Assert - для коротких номеров возвращается "****"
        assertEquals("****", masked);
    }

    @Test
    void maskCardNumber_Null() {
        // Act - метод НЕ бросает исключение, а возвращает "****"
        String result = CardUtil.maskCardNumber(null);

        // Assert
        assertEquals("****", result);
    }

    @Test
    void maskCardNumber_Empty() {
        // Act
        String result = CardUtil.maskCardNumber("");

        // Assert
        assertEquals("****", result);
    }

    @Test
    void maskCardNumber_ExactFourDigits() {
        // Arrange
        String cardNumber = "1234";

        // Act
        String masked = CardUtil.maskCardNumber(cardNumber);

        // Assert
        assertEquals("**** **** **** 1234", masked);
    }
}