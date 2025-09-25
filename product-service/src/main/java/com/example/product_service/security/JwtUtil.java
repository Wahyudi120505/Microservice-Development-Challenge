package com.example.product_service.security;

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
}
