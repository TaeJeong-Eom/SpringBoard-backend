package com.react.board.security;

import jakarta.servlet.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res,
                         AuthenticationException ex) throws IOException {
        // 토큰 만료면 440, 그 외 401
        int status = (ex.getCause() instanceof io.jsonwebtoken.ExpiredJwtException) ? 440 : 401;
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(
                String.format("{\"errorCode\":\"AUTHENTICATION_FAILED\",\"message\":\"%s\"}", ex.getMessage())
        );
    }
}
