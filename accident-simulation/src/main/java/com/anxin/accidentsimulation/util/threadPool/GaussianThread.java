package com.anxin.accidentsimulation.util.threadPool;

import com.anxin.accidentsimulation.util.common.CacheManager;
import com.anxin.accidentsimulation.entity.calculate.LeakageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: ly
 * @date: 2020/1/9 14:42
 */
public class GaussianThread implements Runnable {
    public static final String threadKey = "threadRunResult";//缓存key拼写
    private String userId;//用户id
    private LeakageModel model;//计算所需的所有参数
    private double stepx;//x轴步长
    private double endx;//x轴最远距离
    private double stepy;//y轴初始步长
    private float[] c;//三个浓度集合,由高到低

    @Override
    public void run() {
        Map<String, Map> resultMap = (Map<String, Map>) CacheManager.get(userId + threadKey);
        double x = 0;//设置X的起始值
        double y1 = this.stepy;//设置Y的终点值
        double valueStart = c[2];//给初始浓度设为要求的最低浓度
        boolean flag = true;//终止标志，当开始计算后，又在X轴上遇到0浓度，则结束计算
        while ((valueStart >= c[2] * 0.5 || flag == true) && x <= endx) {
            LeakageModel allParam = model.myClone();
            x += stepx;//x步进
            allParam.setX(x);
            allParam.setY(0);//只求x轴的浓度值，如果轴线为0，则y递增后浓度还是为0
            valueStart = allParam.getValue();
            if (valueStart > c[0]) {
                double stepy = this.stepy;
                Map<String, Object> ndAndCoord = resultMap.get("nd1");
                List<double[]> pointList;
                if (null == ndAndCoord) {
                    ndAndCoord = new ConcurrentHashMap<>();
                    ndAndCoord.put("nd", c[0]);
                    pointList = new ArrayList<>();
                } else {
                    pointList = (List<double[]>) ndAndCoord.get("coord");
                }
                getConByColor(pointList, allParam, 0, y1, c[0], stepy);
                ndAndCoord.put("coord", pointList);
                resultMap.put("nd1", ndAndCoord);
            }
            if (valueStart > c[1]) {
                double stepy = this.stepy;
                Map<String, Object> ndAndCoord = resultMap.get("nd2");
                List<double[]> pointList;
                if (null == ndAndCoord) {
                    ndAndCoord = new ConcurrentHashMap<>();
                    ndAndCoord.put("nd", c[1]);
                    pointList = new ArrayList<>();
                } else {
                    pointList = (List<double[]>) ndAndCoord.get("coord");
                }
                getConByColor(pointList, allParam, 0, y1, c[1], stepy);
                ndAndCoord.put("coord", pointList);
                resultMap.put("nd2", ndAndCoord);
            }
            if (valueStart > c[2]) {
                double stepy = this.stepy;
                Map<String, Object> ndAndCoord = resultMap.get("nd3");
                List<double[]> pointList;
                if (null == ndAndCoord) {
                    ndAndCoord = new ConcurrentHashMap<>();
                    ndAndCoord.put("nd", c[2]);
                    pointList = new ArrayList<>();
                } else {
                    pointList = (List<double[]>) ndAndCoord.get("coord");
                }
                getConByColor(pointList, allParam, 0, y1, c[2], stepy);
                ndAndCoord.put("coord", pointList);
                resultMap.put("nd3", ndAndCoord);
                flag = false;//当X轴上的浓度再为0时，循环结束
            }
        }
        RunningStatus.deleteRunningTask();
    }

    private void getConByColor(List<double[]> pointList, LeakageModel model, double y0, double y1, double color, double stepy) {
        double valueFirst;
        double valueLast = 100;
        double[] point = new double[2];
        double[] minusPoint = new double[2];//x的轴对称点，y的值为负
        while (Math.abs(valueLast - color) > color * 0.1) {
            model.setChange(y0);
            valueFirst = model.getValue();
            model.setChange(y1);
            valueLast = model.getValue();
            if (valueLast > color) {
                y0 = y1;
                y1 = y1 + stepy;
            } else if (valueFirst > color && valueLast < color) {
                stepy = stepy * 0.5;
                y1 = y0 + stepy;
            } else if (valueFirst < color) {
                y1 = y0;
                y0 = y0 - stepy;
            } else {
                System.out.println("浓度不在范围内！");
                return;
            }
        }
        point[0] = model.getX();//x坐标
        point[1] = y1;//y坐标
        minusPoint[0] = model.getX();//x坐标
        minusPoint[1] = -y1;//y坐标
        pointList.add(point);//xy坐标集合
        pointList.add(minusPoint);//x的轴对称点
    }

    public GaussianThread(LeakageModel model, float stepx, float stepy, float endx, float[] c, String userId) {
        if (CacheManager.get(userId + threadKey) == null) {
            CacheManager.getInstance().put(userId + threadKey, new ConcurrentHashMap<>());
        }
        this.model = model;
        this.stepx = stepx;
        this.stepy = stepy;
        this.endx = endx;
        this.c = c;
        this.userId = userId;
    }

}
