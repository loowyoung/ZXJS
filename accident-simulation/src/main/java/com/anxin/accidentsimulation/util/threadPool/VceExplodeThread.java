package com.anxin.accidentsimulation.util.threadPool;

import com.anxin.accidentsimulation.util.common.CacheManager;
import com.anxin.accidentsimulation.entity.calculate.VceExplodeModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Vce蒸汽云爆炸 线程计算类
 *
 * @author: ly
 * @date: 2020/1/9 8:36
 */
public class VceExplodeThread implements Runnable {
    public static final String threadKey = "threadRunResult";//缓存key拼写
    private String userId;//用户id
    private VceExplodeModel model;//计算所需的所有参数
    private float start;//爆炸的起始半径
    private float end;//爆炸的结束超压
    private float step;//爆炸的计算步长
    private float[] c;//爆炸超压集合,由高到低

    @Override
    public void run() {
        Map<String, Map> resultMap = (Map<String, Map>) CacheManager.get(userId + threadKey);
        this.model.setChange(start);
        double valueStart = this.model.getValue();
        if (valueStart > c[0]) {
            Map<String, Object> ndAndCoord = resultMap.get("nd1");
            float bombR;//爆炸半径
            if (null == ndAndCoord) {
                ndAndCoord = new ConcurrentHashMap<>();
                ndAndCoord.put("nd", c[0]);
                bombR = 0;
            } else {
                bombR = (float) ndAndCoord.get("coord");
            }
            bombR = getConByColor(c[0], bombR);
            ndAndCoord.put("coord", bombR);
            resultMap.put("nd1", ndAndCoord);
        }
        if (valueStart > c[1]) {
            Map<String, Object> ndAndCoord = resultMap.get("nd2");
            float bombR;//爆炸半径
            if (null == ndAndCoord) {
                ndAndCoord = new ConcurrentHashMap<>();
                ndAndCoord.put("nd", c[1]);
                bombR = 0;
            } else {
                bombR = (float) ndAndCoord.get("coord");
            }
            bombR = getConByColor(c[1], bombR);
            ndAndCoord.put("coord", bombR);
            resultMap.put("nd2", ndAndCoord);
        }
        if (valueStart > c[2]) {
            Map<String, Object> ndAndCoord = resultMap.get("nd3");
            float bombR;//爆炸半径
            if (null == ndAndCoord) {
                ndAndCoord = new ConcurrentHashMap<>();
                ndAndCoord.put("nd", c[2]);
                bombR = 0;
            } else {
                bombR = (float) ndAndCoord.get("coord");
            }
            bombR = getConByColor(c[2], bombR);
            ndAndCoord.put("coord", bombR);
            resultMap.put("nd3", ndAndCoord);
        }
        RunningStatus.deleteRunningTask();
    }

    /**
     * 根据给出的超压，求半径
     *
     * @param color
     * @param bombR
     */
    private float getConByColor(float color, float bombR) {
        this.model.setChange(start);
        double valueLast = this.model.getValue();
        while (Math.abs(valueLast - color) > color * 0.1 && valueLast >= end) {
            start += step;
            this.model.setChange(start);
            valueLast = this.model.getValue();
            bombR = start;
        }
        return bombR;
    }

    public VceExplodeThread(VceExplodeModel model, float start, float end, float step, float[] c, String userId) {
        if (CacheManager.get(userId + threadKey) == null) {
            CacheManager.getInstance().put(userId + threadKey, new ConcurrentHashMap<>());
        }
        this.model = model;
        this.start = start;
        this.end = end;
        this.step = step;
        this.c = c;
        this.userId = userId;
    }

}
