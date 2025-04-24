package com.example.stu.Accounts.Controllers;

import com.example.stu.Core.Auth.RequireUserType;
import com.example.stu.Core.Enums.UserType;
import com.example.stu.Core.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for customer-only operations
 * The entire controller is protected with @RequireUserType(UserType.CUSTOMER)
 */
@RestController
@RequestMapping("/api/customer")
@RequireUserType(UserType.CUSTOMER)
// Access control is now handled in JwtAuthInterceptor
public class CustomerController {
    
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<String>> getCustomerDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Customer dashboard data", "Customer dashboard retrieved successfully"));
    }
    
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<String>> getCustomerStats() {
        return ResponseEntity.ok(ApiResponse.success("Customer statistics data", "Customer statistics retrieved successfully"));
    }
    
    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<String>> getCustomerSettings() {
        return ResponseEntity.ok(ApiResponse.success("Customer settings data", "Customer settings retrieved successfully"));
    }
}