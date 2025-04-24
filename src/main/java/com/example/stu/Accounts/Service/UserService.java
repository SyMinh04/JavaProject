package com.example.stu.Accounts.Service;

import com.example.stu.Accounts.Mappers.UserMapper;
import com.example.stu.Accounts.Models.User;
import com.example.stu.Accounts.Repositories.UserRepository;
import com.example.stu.Accounts.Serializers.Requests.CreateUserRequest;
import com.example.stu.Accounts.Serializers.Requests.LoginRequest;
import com.example.stu.Accounts.Serializers.Requests.RefreshTokenRequest;
import com.example.stu.Accounts.Serializers.Requests.UpdateUserRequest;
import com.example.stu.Accounts.Serializers.Responses.LoginResponse;
import com.example.stu.Core.Exceptions.ResourceNotFoundException;
import com.example.stu.Core.Exceptions.UnauthorizedException;
import com.example.stu.Core.Service.BaseService;
import com.example.stu.Core.Auth.JwtUtility;
import com.example.stu.Core.Utils.MessageUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService extends BaseService<User, UUID, UserRepository> {
    
    private final UserMapper userMapper;
    private final JwtUtility jwtUtility;
    private final MessageUtils messageUtils;
    
    public UserService(UserRepository repository, UserMapper userMapper, JwtUtility jwtUtility, MessageUtils messageUtils) {
        super(repository);
        this.userMapper = userMapper;
        this.jwtUtility = jwtUtility;
        this.messageUtils = messageUtils;
    }
    
    public User createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        user.makePassword(request.getPassword());
        return repository.save(user);
    }
    
    public LoginResponse login(LoginRequest request) {
        User user = repository.findByGmail(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtils.getMessage("user.not.found")));
        
        if (!user.comparePassword(request.getPassword())) {
            throw new UnauthorizedException(messageUtils.getMessage("password_is_not_correct"));
        }
        
        String accessToken = jwtUtility.getAccessToken(user.getGmail(), user.getUid(), user.getUserType());
        String refreshToken = jwtUtility.getRefreshToken(user.getGmail(), user.getUid());
        
        return LoginResponse.builder()
                .userId(user.getUid())
                .username(user.getGmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getUserType())
                .expiresIn(jwtUtility.getAccessTokenExpirationTime())
                .build();
    }
    
    public User updateUser(UUID id, UpdateUserRequest request) {
        User existingUser = findById(id);
        User updatedUser = userMapper.updateEntity(existingUser, request);
        return repository.save(updatedUser);
    }
    
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        
        if (!jwtUtility.validateRefreshToken(refreshToken)) {
            throw new UnauthorizedException(messageUtils.getMessage("user.invalid.credentials"));
        }
        
        try {
            String username = jwtUtility.extractUsername(refreshToken);
            String userId = jwtUtility.extractUserId(refreshToken);
            
            User user = repository.findByGmail(username)
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtils.getMessage("user.not.found")));
            
            String newAccessToken = jwtUtility.getAccessToken(username, user.getUid(), user.getUserType());
            String newRefreshToken = jwtUtility.getRefreshToken(username, user.getUid());
            
            return LoginResponse.builder()
                    .userId(user.getUid())
                    .username(username)
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .role(user.getUserType())
                    .expiresIn(jwtUtility.getAccessTokenExpirationTime())
                    .build();
        } catch (Exception e) {
            throw new UnauthorizedException(messageUtils.getMessage("user.invalid.credentials"));
        }
    }

}
