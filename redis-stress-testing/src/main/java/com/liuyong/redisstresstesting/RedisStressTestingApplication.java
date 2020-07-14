package com.liuyong.redisstresstesting;

import com.liuyong.redisstresstesting.service.RedisGetService;
import com.liuyong.redisstresstesting.service.RedisSaveService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class RedisStressTestingApplication implements ApplicationRunner {

    @Resource
    private RedisSaveService redisSaveService;
    @Resource
    private RedisGetService redisGetService;

    public static void main(String[] args) {
        SpringApplication.run(RedisStressTestingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        redisSaveService.testRedisSave();
        //redisGetService.find();
    }
}
