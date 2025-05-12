package com.react.board.controller;

import com.react.board.dto.AuthRequest;
import com.react.board.dto.AuthResponse;
import com.react.board.security.JwtService;
import java.util.Date;

import com.react.board.security.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthController(JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

// 리프레시 토큰을 HttpOnly 쿠키로 설정
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);// HTTPS 환경에서만 설정
        refreshCookie.setPath("/api/auth");// 리프레시 API 경로에만 전송
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);// 7일
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue("refresh_token") String refreshToken) {
        String username = jwtService.extractUsername(refreshToken, true);

        if (username != null && jwtService.validateToken(refreshToken, username, true)) {
            String newAccessToken = jwtService.generateAccessToken(username);
            return ResponseEntity.ok(new AuthResponse(newAccessToken));
        }

        return ResponseEntity.status(401).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader,
                                       HttpServletResponse response) {
        // 액세스 토큰 블랙리스트에 추가
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            // JWT에서 남은 유효 시간 계산
            String username = jwtService.extractUsername(accessToken, false);
            if (username != null) {
                try {
                    Date expiration = jwtService.extractExpiration(accessToken, false);
                    long ttl = expiration.getTime() - System.currentTimeMillis();
                    if (ttl > 0) {
                        refreshTokenService.blacklist(accessToken, ttl); // tokenBlacklistService -> refreshTokenService
                    }
                } catch (Exception e) {
                    // 만료된 토큰이면 무시
                }
            }
        }

        // 리프레시 토큰 쿠키 삭제
        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/api/auth");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok().build();
    }
}
