package com.example.stu.Accounts.Serializers.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID uid;
    private String gmail;
    private String user_type;
}