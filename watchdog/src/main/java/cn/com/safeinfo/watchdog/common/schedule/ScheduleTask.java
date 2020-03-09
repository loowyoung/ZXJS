package cn.com.safeinfo.watchdog.common.schedule;

import cn.com.safeinfo.watchdog.model.entity.ModuleModel;
import cn.com.safeinfo.watchdog.service.ModuleService;
import cn.com.safeinfo.watchdog.service.MonitorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.List;

/**
 * 定时任务：检查服务状态，获取服务器信息
 *
 * @author: ly
 * @date: 2020/3/6 21:29
 */
@Component
@Configuration
@EnableScheduling
public class ScheduleTask {
    @Resource
    MonitorService monitorService;
    @Resource
    ModuleService moduleService;


    //每10分钟执行一次
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void exec() {
        System.out.println("每隔10分钟执行一次： " + LocalTime.now());
        getInfo();
        checkModuleStatus();
    }

    /**
     * 获取系统资源使用率
     */
    private void getInfo() {
        monitorService.getserverInfo();
    }

    /**
     * 检查所有模块是否启动
     */
    private void checkModuleStatus() {
        List<ModuleModel> modelList = moduleService.list();
        modelList.forEach(ml -> {
            String moduleName = ml.getServiceName();
            moduleService.queryModule(moduleName);
        });
    }
}
