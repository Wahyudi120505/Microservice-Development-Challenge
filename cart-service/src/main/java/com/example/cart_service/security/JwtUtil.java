package com.example.cart_service.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
@SuppressWarnings("deprecation")
public class JwtUtil {
    private final String SECRET_KEY = "mysuperlongsecretkeyformyjwtthatismorethan32chars";

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractRole(String token) {
        return (String) extractClaims(token).get("role");
    }

    public Integer extractUserId(String token) {
        Object userId = extractClaims(token).get("userId"); 
        if (userId instanceof Integer) {
            return (Integer) userId;
        } else if (userId instanceof String) {
            return Integer.parseInt((String) userId);
        } else {
            throw new RuntimeException("Invalid userId in token");
        }
    }
}
