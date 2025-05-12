package com.react.board.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserDetailsService uds,
                                   RefreshTokenService rts) {
        this.jwtService = jwtService;
        this.userDetailsService = uds;
        this.refreshTokenService = rts;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req,res);
            return;
        }

        String token = header.substring(7);
        // 액세스 토큰 블랙리스트 확인
        if (refreshTokenService.isBlacklisted(token)) { // 이름 변경
            chain.doFilter(req,res);
            return; // 이미 로그아웃된 토큰
        }
        try {
            String username = jwtService.extractUsername(token,false);
            if (username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
                UserDetails ud = userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, username,false)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(ud,null,ud.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception ex) {
            // 토큰 만료 등 오류 시 그냥 넘어가서 EntryPoint에서 처리
        }

        chain.doFilter(req,res);
    }
}
