package com.example.stu.Core.Auth;

import com.example.stu.Core.Config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtility {
    
    /**
     * Get the signing key for access tokens
     * @return SecretKey for signing access tokens
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = JwtConfig.JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Get the signing key for refresh tokens
     * @return SecretKey for signing refresh tokens
     */
    private SecretKey getRefreshSigningKey() {
        byte[] keyBytes = JwtConfig.JWT_REFRESH_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String getAccessToken(String username, UUID userId, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConfig.USER_ID_CLAIM, userId.toString());
        claims.put(JwtConfig.USER_TYPE_CLAIM, userType);
        claims.put(JwtConfig.TOKEN_TYPE_CLAIM, JwtConfig.ACCESS_TOKEN_TYPE);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtConfig.ACCESS_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String getRefreshToken(String username, UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConfig.USER_ID_CLAIM, userId.toString());
        claims.put(JwtConfig.TOKEN_TYPE_CLAIM, JwtConfig.REFRESH_TOKEN_TYPE);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtConfig.REFRESH_TOKEN_VALIDITY))
                .signWith(getRefreshSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get(JwtConfig.USER_ID_CLAIM, String.class));
    }
    
    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get(JwtConfig.TOKEN_TYPE_CLAIM, String.class));
    }

    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get(JwtConfig.USER_TYPE_CLAIM, String.class));
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    private Claims extractAllRefreshClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getRefreshSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public Boolean validateAccessToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            final String tokenType = extractTokenType(token);
            return extractedUsername.equals(username) && 
                   JwtConfig.ACCESS_TOKEN_TYPE.equals(tokenType) && 
                   !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    public Boolean validateRefreshToken(String token) {
        try {
            final Claims claims = extractAllRefreshClaims(token);
            final String tokenType = claims.get(JwtConfig.TOKEN_TYPE_CLAIM, String.class);
            final Date expiration = claims.getExpiration();
            
            return JwtConfig.REFRESH_TOKEN_TYPE.equals(tokenType) && 
                   !expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    public long getAccessTokenExpirationTime() {
        return JwtConfig.ACCESS_TOKEN_VALIDITY / 1000; // Convert to seconds
    }
}