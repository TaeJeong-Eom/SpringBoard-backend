package com.react.board.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenService {

    private final ValueOperations<String, String> ops;
    private static final String PREFIX = "blk_refresh_"; // 블랙리스트 키 접두사

    public RefreshTokenService(RedisTemplate<String, String> redisTemplate) {
        this.ops = redisTemplate.opsForValue();
    }

    public void blacklist(String token, long ttlMillis) {
        Duration duration = Duration.ofMillis(ttlMillis);
        ops.set(PREFIX + token, "blacklisted", duration);
    }

    public boolean isBlacklisted(String token) {
        return ops.get(PREFIX + token) != null;
    }
}