package cn.com.safeinfo.watchdog.service;

import cn.com.safeinfo.watchdog.common.result.JsonResult;
import cn.com.safeinfo.watchdog.common.result.ResultUtil;
import cn.com.safeinfo.watchdog.common.util.FileUtil;
import cn.com.safeinfo.watchdog.common.util.SystemInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
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
    public JsonResult getserverInfo() {
        long start = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        result.put("CPU", SystemInfoUtil.getServerCpuUsage());
        result.put("Memory", SystemInfoUtil.getServerMemoryUsage());
        result.put("Disk", SystemInfoUtil.getServerDiskUsage());
        result.put("time", LocalTime.now());
        long end = System.currentTimeMillis();
        logger.debug("获取服务器的使用率耗时：{}ms", end - start);
        return ResultUtil.success(result);
    }

    /**
     * 获取单个进程的信息
     *
     * @param moduleName 服务名
     * @return
     */
    public JsonResult getServiceInfo(String moduleName) {
        long start = System.currentTimeMillis();
        String pidPath = FileUtil.watchPath + File.separator + moduleName + ".pid";//进程号文件
        File pidFile = new File(pidPath);
        if (!pidFile.exists()) {
            return ResultUtil.error(500, "服务已停止");
        }
        String pid = FileUtil.readFile(pidFile);
        Map<String, Object> result = new HashMap<>();
        result.put("CPU", SystemInfoUtil.getServiceCpuUsage(Integer.valueOf(pid)));
        result.put("Memory", SystemInfoUtil.getServiceMemoryUsage(Integer.valueOf(pid)));
        result.put("TCP", SystemInfoUtil.getTcpCount(Integer.valueOf(pid)));
        result.put("time", LocalTime.now());
        long end = System.currentTimeMillis();
        logger.debug("获取{}服务的使用率耗时：{}ms", moduleName, end - start);
        return ResultUtil.success(result);
    }
}
