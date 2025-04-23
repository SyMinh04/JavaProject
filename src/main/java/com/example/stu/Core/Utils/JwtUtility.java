package com.example.stu.Core.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtility {
    
    private static final long ACCESS_TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 hours
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 days
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    public String getAccessToken(String username, UUID userId, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("userType", userType);
        claims.put("tokenType", "access");
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }
    
    public String getRefreshToken(String username, UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("tokenType", "refresh");
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(refreshKey)
                .compact();
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }
    
    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("tokenType", String.class));
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    
    private Claims extractAllRefreshClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token).getBody();
    }
    
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public Boolean validateAccessToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            final String tokenType = extractTokenType(token);
            return extractedUsername.equals(username) && 
                   "access".equals(tokenType) && 
                   !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    public Boolean validateRefreshToken(String token) {
        try {
            final Claims claims = extractAllRefreshClaims(token);
            final String tokenType = claims.get("tokenType", String.class);
            final Date expiration = claims.getExpiration();
            
            return "refresh".equals(tokenType) && 
                   !expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    public long getAccessTokenExpirationTime() {
        return ACCESS_TOKEN_VALIDITY / 1000; // Convert to seconds
    }
}