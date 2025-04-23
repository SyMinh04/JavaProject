package com.example.stu.Accounts.Serializers.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private UUID userId;
    private String username;
    private String accessToken;
    private String refreshToken;
    private String role;
    private long expiresIn;
}