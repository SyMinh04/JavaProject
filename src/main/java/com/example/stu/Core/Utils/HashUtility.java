package com.example.stu.Core.Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utility class for hashing data
 */
public class HashUtility {

    /**
     * Generates a hash for the input data with a default length of 16 characters
     *
     * @param data The data to hash
     * @return A 16-character hash string
     */
    public static String hashData(String data) {
        return hashData(data, 16);
    }

    /**
     * Generates a hash for the input data with a specified length
     *
     * @param data The data to hash
     * @param length The desired length of the hash string
     * @return A hash string of the specified length
     */
    public static String hashData(String data, int length) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data to hash cannot be null or empty");
        }
        
        if (length <= 0) {
            throw new IllegalArgumentException("Hash length must be greater than 0");
        }
        
        try {
            // Use SHA-256 for hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            
            // Convert to Base64 to get a string representation
            String base64Hash = Base64.getEncoder().encodeToString(hashBytes);
            
            // Remove non-alphanumeric characters
            String alphanumericHash = base64Hash.replaceAll("[^a-zA-Z0-9]", "");
            
            // Return the hash with the specified length
            return alphanumericHash.substring(0, Math.min(length, alphanumericHash.length()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate hash: " + e.getMessage(), e);
        }
    }
}