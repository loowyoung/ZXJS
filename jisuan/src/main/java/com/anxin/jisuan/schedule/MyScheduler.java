package com.anxin.jisuan.schedule;

import com.anxin.jisuan.job.PrintWordsJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 任务调度
 *
 * @author: ly
 * @date: 2019/11/12 11:01
 * https://blog.csdn.net/noaman_wgs/article/details/80984873
 */
public class MyScheduler {
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        //1.创建调度器Scheduler
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        //2.创建JobDetail实例，并与PrintWordsJob类绑定（Job执行内容）
        JobDetail jobDetail = JobBuilder.newJob(PrintWordsJob.class).withIdentity("job1", "group1").build();
        //3.构建Trigger实例，每隔1s执行一次
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(1)//每隔1s执行一次
                .repeatForever();//一直执行
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
                .startNow()//立即生效
                .withSchedule(simpleScheduleBuilder)
                .build();
        //4.执行
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("------定时任务开始------");
        scheduler.start();
        //睡眠一分钟后结束
        TimeUnit.MINUTES.sleep(1);
        scheduler.shutdown();
        System.out.println("------定时任务结束------");

    }
}
