package com.gateway.npay.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private String expiration;

    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        long expirationTime = 1000 * 60 * 60; // 1 hour
        Date expiryDate = new Date(System.currentTimeMillis() + expirationTime);
        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(expiryDate).signWith(key(), SignatureAlgorithm.HS256).compact();

    }

    public String extractUsername(String token){
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }
}
