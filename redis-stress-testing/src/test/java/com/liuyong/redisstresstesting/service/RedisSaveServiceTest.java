package com.liuyong.redisstresstesting.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisSaveServiceTest {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void setTest() throws Exception {

        redisTemplate.opsForValue().set("ok", "test");
        System.out.println(
                "setTest:" + redisTemplate.opsForValue().get("ok")
        );
    }

}