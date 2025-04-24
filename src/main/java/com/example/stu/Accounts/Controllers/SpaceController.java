package com.example.stu.Accounts.Controllers;

import com.example.stu.Core.Auth.RequireUserType;
import com.example.stu.Core.Enums.UserType;
import com.example.stu.Core.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for space-only operations
 * The entire controller is protected with @RequireUserType(UserType.SPACE)
 */
@RestController
@RequestMapping("/api/space")
@RequireUserType({UserType.SPACE})
// Access control is now handled in JwtAuthInterceptor
public class SpaceController {
    
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<String>> getSpaceDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Space dashboard data", "Space dashboard retrieved successfully"));
    }
    
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<String>> getSpaceStats() {
        return ResponseEntity.ok(ApiResponse.success("Space statistics data", "Space statistics retrieved successfully"));
    }
    
    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<String>> getSpaceSettings() {
        return ResponseEntity.ok(ApiResponse.success("Space settings data", "Space settings retrieved successfully"));
    }
}