package com.example.stu.Accounts.Mappers;

import com.example.stu.Accounts.Models.User;
import com.example.stu.Accounts.Serializers.Requests.CreateUserRequest;
import com.example.stu.Accounts.Serializers.Requests.UpdateUserRequest;
import com.example.stu.Core.Mapper.GenericEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends GenericEntityMapper<User, CreateUserRequest, UpdateUserRequest> {
    
    public UserMapper() {
        super(User.class);
    }
}