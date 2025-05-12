package com.react.board.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    // 액세스 토큰 생성
    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenSecret, accessTokenExpiration);
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenSecret, refreshTokenExpiration);
    }

    // 토큰 생성 공통 메서드
    private String generateToken(String subject, String secret, long expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 사용자명 추출
    public String extractUsername(String token, boolean isRefreshToken) {
        try {
            String secret = isRefreshToken ? refreshTokenSecret : accessTokenSecret;
            return extractClaim(token, Claims::getSubject, secret);
        } catch (Exception e) {
            return null; // 오류 시 null 반환
        }
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token, String username, boolean isRefreshToken) {
        String secret = isRefreshToken ? refreshTokenSecret : accessTokenSecret;
        final String extractedUsername = extractUsername(token, isRefreshToken);
        return extractedUsername != null && extractedUsername.equals(username) && !isTokenExpired(token, secret);
    }

    // 토큰 만료 확인
    private boolean isTokenExpired(String token, String secret) {
        return extractExpiration(token, secret).before(new Date());
    }

    // 토큰에서 만료 시간 추출
    public Date extractExpiration(String token, String secret) {
        return extractClaim(token, Claims::getExpiration, secret);
    }

    // 토큰에서 만료 시간 추출 (boolean 파라미터 버전)
    public Date extractExpiration(String token, boolean isRefreshToken) {
        String secret = isRefreshToken ? refreshTokenSecret : accessTokenSecret;
        return extractExpiration(token, secret);
    }

    // 클레임 추출 헬퍼 메서드
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secret) {
        final Claims claims = extractAllClaims(token, secret);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}