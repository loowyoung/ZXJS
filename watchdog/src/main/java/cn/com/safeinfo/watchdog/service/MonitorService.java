package cn.com.safeinfo.watchdog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author: ly
 * @date: 2020/3/4 14:20
 */
public class MonitorService {
    @Autowired
    private MongoTemplate mongoTemplate;
    
}
