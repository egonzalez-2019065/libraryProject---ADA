package com.alexandergonzalez.libraryProject.config;

import com.alexandergonzalez.libraryProject.exceptions.exception.InvalidJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "zf0V5uuWc8gS06vQBpthZYP3iu8fPGS1q71S7pET1aAenHGyzDRyiftGYQBgUN7wdcWeqr7ATddzZa9ZWd6SbRPmY";

    public String getToken(UserDetails user){
        return  getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        try {
            String username = getClaim(token, Claims::getSubject);
            System.out.println("Token: " + token);
            System.out.println("Username extraído: " + username);
            return username;
        } catch (Exception e) {
            System.out.println("Error extrayendo username del token: " + e.getMessage());
            throw new InvalidJWT("Token inválido");
        }
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        if (username == null || !username.equals(userDetails.getUsername())) {
            System.out.println("Username no coincide: token=" + username + ", user=" + userDetails.getUsername());
            return false;
        }
        if (isTokenExpired(token)) {
            System.out.println("Token expirado.");
            return false;
        }
        return true;
    }


    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
