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
import com.example.stu.Core.Auth.RequireUserType;
import com.example.stu.Core.Controller.BaseController;
import com.example.stu.Core.Enums.UserType;
import com.example.stu.Core.Response.ApiResponse;
import com.example.stu.Core.Utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.stu.Core.Auth.PublicApi;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequireUserType({UserType.USER}) // Protect entire controller - only ADMIN can access by default
public class UserController extends BaseController<User, UUID, UserService> {

    private final UserMapper userMapper;
    private final MessageUtils messageUtils;

    public UserController(UserService service, UserMapper userMapper, MessageUtils messageUtils) {
        super(service);
        this.userMapper = userMapper;
        this.messageUtils = messageUtils;
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<User> users = service.findAll();
        List<UserResponse> responses = users.stream()
                .map(user -> userMapper.toResponse(user, UserResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses, messageUtils.getMessage("users.retrieved.success")));
    }

    @GetMapping("/space-only")
    @RequireUserType({UserType.SPACE}) // Only SPACE can access
    public ResponseEntity<ApiResponse<String>> getSpaceOnlyData() {
        return ResponseEntity.ok(ApiResponse.success("This data is only accessible by SPACE role", "Space data retrieved successfully"));
    }

    @GetMapping("/customer-only")
    @RequireUserType({UserType.CUSTOMER}) // Only CUSTOMER can access
    public ResponseEntity<ApiResponse<String>> getCustomerOnlyData() {
        return ResponseEntity.ok(ApiResponse.success("This data is only accessible by CUSTOMER role", "Customer data retrieved successfully"));
    }

    @GetMapping("/shared-endpoint")
    @RequireUserType({UserType.ADMIN, UserType.SPACE}) // Both ADMIN and SPACE can access
    public ResponseEntity<ApiResponse<String>> getSharedData() {
        return ResponseEntity.ok(ApiResponse.success("This data is accessible by both ADMIN and SPACE roles", "Shared data retrieved successfully"));
    }
//
//    @Override
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable UUID id) {
//        User user = service.findById(id);
//        UserResponse response = userMapper.toResponse(user, UserResponse.class);
//        return ResponseEntity.ok(ApiResponse.success(response, messageUtils.getMessage("user.retrieved.success")));
//    }

    @PostMapping
    @PublicApi
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody CreateUserRequest request) {
        User createdUser = service.createUser(request);
        UserResponse response = userMapper.toResponse(createdUser, UserResponse.class);
        return new ResponseEntity<>(ApiResponse.success(response, messageUtils.getMessage("user.created.success")), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @PublicApi // Allow public login
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = service.login(request);
        return ResponseEntity.ok(ApiResponse.success(loginResponse, messageUtils.getMessage("user.login.success")));
    }

    @PostMapping("/refresh-token")
    @PublicApi // Allow public token refresh
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
