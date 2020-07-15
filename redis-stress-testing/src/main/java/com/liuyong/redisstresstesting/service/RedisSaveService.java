package com.liuyong.redisstresstesting.service;

import com.liuyong.redisstresstesting.common.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Redis保存类
 *
 * @author ly
 * @date 2020年 07月08日 16:55:57
 */
@Service
public class RedisSaveService {
    @Resource
    private RedisUtil redisUtil;

    public void testRedisSave() {
        Vector<Thread> threadVector = new Vector<>();
        long step1 = 0;
        try {
            step1 = System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                //开线程，监听消费
                int finalI = i;
                Thread t = new Thread(() -> {
                    save(finalI);
                });
                threadVector.add(t);
                t.start();
                System.out.println("线程" + finalI + "启动");
            }
            for (Thread thread : threadVector) {
                thread.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long step2 = System.currentTimeMillis();
        System.out.println("运行完" + threadVector.size() + "个线程，耗时：" + (step2 - step1) + "ms");
    }

    private void save(int index) {
        int step = 2000;
        long step1 = System.currentTimeMillis();
        int i = index * step;//起始值
        int end = i + step;//终止值
        Map<String, Map<byte[], byte[]>> allData = new HashMap<>();
        while (i < end) {
            Map<byte[], byte[]> param = new HashMap<>();
            param.put("pipeline1".getBytes(), "pipeline1".getBytes());
            param.put("pipeline2".getBytes(), "pipeline2".getBytes());
            param.put("pipeline3".getBytes(), "pipeline3".getBytes());
            param.put("pipeline4".getBytes(), "pipeline4".getBytes());
            param.put("pipeline5".getBytes(), "pipeline5".getBytes());
            param.put(null,null);
            allData.put("ly:newtest" + i, param);
            i++;
        }

        redisUtil.saveSetPipe("hrmwv2:m:t:r", allData.keySet());

        redisUtil.saveHashPipe(allData);
        long step2 = System.currentTimeMillis();
        System.out.println("线程：" + index + "存储了" + step + "条数据，耗时：" + (step2 - step1) + "ms");
    }

    public static void main(String[] args) {
        Map test = new HashMap();
        test.put(null,null);
        boolean empty = test.isEmpty();
        System.out.println(empty);

    }
}
