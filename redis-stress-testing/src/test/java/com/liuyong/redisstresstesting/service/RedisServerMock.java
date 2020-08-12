package com.liuyong.redisstresstesting.service;

import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * redis mock
 *
 * @author ly
 * @date 2020年 08月12日 09:59:04
 */
@Component
public class RedisServerMock {

    private RedisServer redisServer;

    /**
     * 构造方法之后执行.
     *
     * @throws IOException
     */
    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    /**
     * 析构方法之后执行.
     */
    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
