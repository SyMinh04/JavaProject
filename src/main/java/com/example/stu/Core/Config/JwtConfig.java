package com.example.stu.Core.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Configuration class for JWT-related constants
 */
@Component
public class JwtConfig {
    /**
     * Access token type identifier
     */
    public static final String ACCESS_TOKEN_TYPE = "access";
    
    /**
     * Refresh token type identifier
     */
    public static final String REFRESH_TOKEN_TYPE = "refresh";
    
    /**
     * User ID claim name
     */
    public static final String USER_ID_CLAIM = "userId";
    
    /**
     * User type claim name
     */
    public static final String USER_TYPE_CLAIM = "userType";
    
    /**
     * Token type claim name
     */
    public static final String TOKEN_TYPE_CLAIM = "tokenType";
    
    /**
     * Validity period for access tokens in milliseconds (from environment)
     */
    public static long ACCESS_TOKEN_VALIDITY;
    
    /**
     * Validity period for refresh tokens in milliseconds (from environment)
     */
    public static long REFRESH_TOKEN_VALIDITY;
    
    /**
     * JWT secret key (from environment)
     */
    public static String JWT_SECRET_KEY;
    
    /**
     * JWT refresh secret key (from environment)
     */
    public static String JWT_REFRESH_SECRET_KEY;
    
    @Autowired
    private EnvConfig envConfig;
    
    /**
     * Initialize static variables from environment config
     */
    @PostConstruct
    public void init() {
        ACCESS_TOKEN_VALIDITY = envConfig.getJwtAccessTokenValidity();
        REFRESH_TOKEN_VALIDITY = envConfig.getJwtRefreshTokenValidity();
        JWT_SECRET_KEY = envConfig.getJwtSecretKey();
        JWT_REFRESH_SECRET_KEY = envConfig.getJwtRefreshSecretKey();
    }
}