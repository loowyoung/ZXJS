package cn.com.safeinfo.watchdog.schedule;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
    //每10分钟执行一次
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void exec() {
        System.out.println("每隔五秒钟执行一次： " + LocalDate.now());
    }
}
