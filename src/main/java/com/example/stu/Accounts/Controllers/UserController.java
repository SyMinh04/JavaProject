package com.example.stu.Accounts.Controllers;

import com.example.stu.Accounts.Mappers.UserMapper;
import com.example.stu.Accounts.Models.User;
import com.example.stu.Accounts.Serializers.Requests.CreateUserRequest;
import com.example.stu.Accounts.Serializers.Requests.LoginRequest;
import com.example.stu.Accounts.Serializers.Requests.RefreshTokenRequest;
import com.example.stu.Accounts.Serializers.Requests.UpdateUserRequest;
import com.example.stu.Accounts.Serializers.Responses.LoginResponse;
import com.example.stu.Accounts.Serializers.Responses.UserResponse;
import com.example.stu.Accounts.Service.UserService;
import com.example.stu.Core.Controller.BaseController;
import com.example.stu.Core.Response.ApiResponse;
import com.example.stu.Core.Utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<User, UUID, UserService> {
    
    private final UserMapper userMapper;
    private final MessageUtils messageUtils;
    
    public UserController(UserService service, UserMapper userMapper, MessageUtils messageUtils) {
        super(service);
        this.userMapper = userMapper;
        this.messageUtils = messageUtils;
    }
    
//    @Override
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
//        List<User> users = service.findAll();
//        List<UserResponse> responses = users.stream()
//                .map(user -> userMapper.toResponse(user, UserResponse.class))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(ApiResponse.success(responses, messageUtils.getMessage("users.retrieved.success")));
//    }
//
//    @Override
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable UUID id) {
//        User user = service.findById(id);
//        UserResponse response = userMapper.toResponse(user, UserResponse.class);
//        return ResponseEntity.ok(ApiResponse.success(response, messageUtils.getMessage("user.retrieved.success")));
//    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody CreateUserRequest request) {
        User createdUser = service.createUser(request);
        UserResponse response = userMapper.toResponse(createdUser, UserResponse.class);
        return new ResponseEntity<>(ApiResponse.success(response, messageUtils.getMessage("user.created.success")), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = service.login(request);
        return ResponseEntity.ok(ApiResponse.success(loginResponse, messageUtils.getMessage("user.login.success")));
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        LoginResponse loginResponse = service.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success(loginResponse, messageUtils.getMessage("user.login.success")));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {
        User updatedUser = service.updateUser(id, request);
        UserResponse response = userMapper.toResponse(updatedUser, UserResponse.class);
        return ResponseEntity.ok(ApiResponse.success(response, messageUtils.getMessage("user.updated.success")));
    }
    
    @Override
    protected String getEntityName() {
        return "User";
    }
}
