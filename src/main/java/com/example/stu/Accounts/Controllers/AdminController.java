package com.example.stu.Accounts.Controllers;

import com.example.stu.Core.Auth.RequireUserType;
import com.example.stu.Core.Enums.UserType;
import com.example.stu.Core.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for admin-only operations
 * The entire controller is protected with @RequireUserType(UserType.ADMIN)
 */
@RestController
@RequestMapping("/api/admin")
@RequireUserType({UserType.ADMIN})
public class AdminController {
    
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<String>> getAdminDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard data", "Admin dashboard retrieved successfully"));
    }
    
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<String>> getAdminStats() {
        return ResponseEntity.ok(ApiResponse.success("Admin statistics data", "Admin statistics retrieved successfully"));
    }
    
    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<String>> getAdminSettings() {
        return ResponseEntity.ok(ApiResponse.success("Admin settings data", "Admin settings retrieved successfully"));
    }
}