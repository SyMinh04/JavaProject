package com.example.stu.Core.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for environment variables
 */
@Configuration
@PropertySource(value = "file:.env", ignoreResourceNotFound = true)
public class EnvConfig {

    @Value("${JWT_SECRET_KEY:defaultSecretKey}")
    private String jwtSecretKey;

    @Value("${JWT_REFRESH_SECRET_KEY:defaultRefreshSecretKey}")
    private String jwtRefreshSecretKey;

    @Value("${JWT_ACCESS_TOKEN_VALIDITY:86400000}")
    private long jwtAccessTokenValidity;

    @Value("${JWT_REFRESH_TOKEN_VALIDITY:604800000}")
    private long jwtRefreshTokenValidity;

    /**
     * Get JWT secret key from environment
     * @return JWT secret key
     */
    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    /**
     * Get JWT refresh secret key from environment
     * @return JWT refresh secret key
     */
    public String getJwtRefreshSecretKey() {
        return jwtRefreshSecretKey;
    }

    /**
     * Get JWT access token validity from environment
     * @return JWT access token validity in milliseconds
     */
    public long getJwtAccessTokenValidity() {
        return jwtAccessTokenValidity;
    }

    /**
     * Get JWT refresh token validity from environment
     * @return JWT refresh token validity in milliseconds
     */
    public long getJwtRefreshTokenValidity() {
        return jwtRefreshTokenValidity;
    }
}