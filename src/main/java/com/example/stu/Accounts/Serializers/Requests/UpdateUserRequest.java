package com.example.stu.Accounts.Serializers.Requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UpdateUserRequest extends CreateUserRequest{
    private String gmail;
    private String password;
    private String userType;
}