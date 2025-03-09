package com.gameofthree.gamemanagerservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameRedisManager {

    private final StringRedisTemplate redisTemplate;

    public GameRedisManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}


