package cn.com.safeinfo.watchdog.service;

import cn.com.safeinfo.watchdog.common.util.SystemInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取运行信息
 *
 * @author: ly
 * @date: 2020/3/6 21:44
 */
@Service
public class MonitorService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorService.class);

    /**
     * 获取服务器的信息
     *
     * @return
     */
    public Map getserverInfo() {
        long start = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        result.put("CPU", SystemInfoUtil.getServerCpuUsage());
        result.put("Memory", SystemInfoUtil.getServerMemoryUsage());
        result.put("Disk", SystemInfoUtil.getServerDiskUsage());
        result.put("time", LocalTime.now());
        long end = System.currentTimeMillis();
        logger.info("获取服务器的使用率耗时：{}ms", end - start);
        return result;
    }

    /**
     * 获取单个进程的信息
     *
     * @param pid
     * @return
     */
    public Map getServiceInfo(int pid) {
        long start = System.currentTimeMillis();
        Map<String, Float> result = new HashMap<>();
        result.put("CPU", SystemInfoUtil.getServiceCpuUsage(pid));
        result.put("Memory", SystemInfoUtil.getServiceMemoryUsage(pid));
        result.put("TCP", Float.valueOf(SystemInfoUtil.getTcpCount(pid)));
        long end = System.currentTimeMillis();
        logger.info("获取服务器的使用率耗时：{}ms", end - start);
        return result;
    }
}
