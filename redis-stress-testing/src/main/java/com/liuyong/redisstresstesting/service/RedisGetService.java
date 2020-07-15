package com.liuyong.redisstresstesting.service;

import com.liuyong.redisstresstesting.common.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis查询
 *
 * @author ly
 * @date 2020年 07月08日 18:13:12
 */
@Service
public class RedisGetService {
    @Resource
    private RedisUtil redisUtil;

    public void find() {
        long step1 = System.currentTimeMillis();
        Set<String> keys = redisUtil.keys("ly*");
        long step2 = System.currentTimeMillis();
        System.out.println("查询到" + keys.size() + "个数据，耗时：" + (step2 - step1) + "ms");
    }

    public void findByPipeline() {
        long step1 = System.currentTimeMillis();
        List<String> keyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            keyList.add("ly:newtest" + i);
        }
        Map<String, Map<String, String>> hashPipe = redisUtil.getHashPipe(keyList);
        long step2 = System.currentTimeMillis();
        System.out.println("耗时："+(step2-step1)+"ms");
    }
}
