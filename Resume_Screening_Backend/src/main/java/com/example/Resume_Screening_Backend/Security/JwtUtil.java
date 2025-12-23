package com.example.Resume_Screening_Backend.Security;

import com.example.Resume_Screening_Backend.Entity.AppUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "mySecretKey123456789012345678901234"; // 32+ chars

    private final Key key =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(AppUser user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}


//@Component
//public class JwtUtil {
//
//    private static final String SECRET_KEY =
//            "mySecretKey123456789012345678901234"; // min 32 chars
//
//    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//
//    // 🔑 TOKEN GENERATION
//    public String generateToken(AppUser user) {
//        return Jwts.builder()
//                .setSubject(user.getUsername())
//                .claim("role", user.getRole())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // 🔍 EXTRACT USERNAME
//    public String extractUsername(String token) {
//        return extractClaims(token).getSubject();
//    }
//
//    // 🔍 EXTRACT ROLE
//    public String extractRole(String token) {
//        return extractClaims(token).get("role", String.class);
//    }
//
//    // 🔐 TOKEN VALIDATION
//    public boolean isTokenValid(String token) {
//        try {
//            extractClaims(token);
//            return true;
//        } catch (JwtException e) {
//            return false;
//        }
//    }
//
//    private Claims extractClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
//
