package com.example.bankcards.service;

import com.example.bankcards.dto.AdminUserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<AdminUserDto> getAllUsersWithCards() {
        List<User> users = userRepository.findAllWithCards();

        return users.stream()
                .map(this::mapToAdminUserDto)
                .toList();
    }

    private AdminUserDto mapToAdminUserDto(User user) {
        AdminUserDto dto = new AdminUserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());

        // Извлекаем ID карт
        List<Long> cardIds = user.getCards() != null
                ? user.getCards().stream()
                .map(card -> card.getId())
                .toList()
                : List.of();

        dto.setCardIds(cardIds);

        return dto;
    }
}