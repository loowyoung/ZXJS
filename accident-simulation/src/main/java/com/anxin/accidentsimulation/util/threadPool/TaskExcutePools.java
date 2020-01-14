package com.anxin.accidentsimulation.util.threadPool;

import com.anxin.accidentsimulation.util.common.CacheManager;
import com.anxin.accidentsimulation.entity.calculate.HeavyGasModel;
import com.anxin.accidentsimulation.entity.calculate.LeakageModel;
import com.anxin.accidentsimulation.entity.calculate.VceExplodeModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 */
public class TaskExcutePools {

    private ThreadPoolExecutor executor = null;

    private static TaskExcutePools taskExcutePools = null;

    private int corePoolSize = 4;

    private int maximumPoolSize = 32;

    public TaskExcutePools() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize,
                    300, TimeUnit.SECONDS, new LinkedBlockingDeque());
        }
        taskExcutePools = this;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public synchronized static TaskExcutePools getInstance() {
        if (taskExcutePools == null) {
            taskExcutePools = new TaskExcutePools();
        }
        return taskExcutePools;
    }

    /**
     * 初始化线程池
     *
     * @param corePoolSize
     * @param maximumPoolSize
     */
    public void initPools(int corePoolSize, int maximumPoolSize) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        executor.setCorePoolSize(this.corePoolSize);
        executor.setMaximumPoolSize(this.maximumPoolSize);
    }

    /**
     * 添加待处理的任务
     *
     * @param task
     */
    public void addTask(Runnable task) {
        RunningStatus.addRunningTask();
        executor.execute(task);
    }


    /**
     * 一次添加一个任务
     * 重气计算
     */
    public static void addHeavyGasTask(List<HeavyGasModel> xList, HeavyGasModel tParam, double stepY, float[] c, int idspl, String userId) {
        TaskExcutePools pools = TaskExcutePools.getInstance();
        //创建算法值计算任务
        HeavyGasThread task = new HeavyGasThread(xList, tParam, stepY, c, idspl, userId);
        pools.addTask(task);
    }

    /**
     * Vce蒸汽云爆炸
     * 添加计算任务
     *
     * @param model  计算参数
     * @param start  起始半径
     * @param end    结束超压值，防止无线计算
     * @param step   计算步长
     * @param c      三个超压范围，由高到低
     * @param userId 用户id
     */
    public static void addVceTask(VceExplodeModel model, float start, float end, float step, float[] c, String userId) {
        TaskExcutePools pools = TaskExcutePools.getInstance();
        VceExplodeThread task = new VceExplodeThread(model, start, end, step, c, userId);
        pools.addTask(task);
    }

    public static void addGaussianTask(LeakageModel model, float stepx, float stepy,float endx, float[] c, String userId) {
        TaskExcutePools pools = TaskExcutePools.getInstance();
        GaussianThread task = new GaussianThread(model, stepx, stepy,endx, c, userId);
        pools.addTask(task);
    }

    /**
     * 获取计算结果
     *
     * @return
     */
    public static Map getStateResult() {
        while (RunningStatus.getIsRunning()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return CacheManager.getInstance().getCache();
    }


}
