package com.example.bankcards.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private List<Long> cardIds;
}