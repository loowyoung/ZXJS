package com.anxin.jisuan.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 定时任务：打印任意内容
 *
 * @author: ly
 * @date: 2019/11/12 10:55
 */
public class PrintWordsJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String pringTime = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("时间：" + pringTime + ", 内容：" + new Random().nextInt(10));
    }
}
