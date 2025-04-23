package com.example.stu.Core.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generic API response wrapper
 * @param <T> Response data type
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Create a success response
     * @param data Response data
     * @param message Success message
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now());
    }
    
    /**
     * Create a success response
     * @param data Response data
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation successful");
    }
    
    /**
     * Create an error response
     * @param message Error message
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now());
    }
}