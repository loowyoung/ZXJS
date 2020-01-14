package com.anxin.accidentsimulation.util.threadPool;

import com.anxin.accidentsimulation.util.common.CacheManager;
import com.anxin.accidentsimulation.entity.calculate.HeavyGasModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 重气泄漏线程计算类
 */
public class HeavyGasThread implements Runnable {
    public static final String threadKey = "threadRunResult";//缓存key拼写
    private String userId;//用户id
    private int idspl;//泄漏类型：2水平泄漏、3垂直泄漏、4瞬时或短时间释放
    private List<HeavyGasModel> xList;//x轴坐标点集合,及x相关参数
    private HeavyGasModel tParam;//单个时间点参数，及t相关参数
    private double stepY;//y轴初始步长
    private float[] c;//梯度浓度集合,由高到低


    @Override
    public void run() {
        Map<String, Map> resultMap = (Map<String, Map>) CacheManager.get(userId + threadKey);
        //循环所有x
        for (HeavyGasModel xParam : this.xList) {
            HeavyGasModel allParam;//所有参数
            if (idspl == 4) {//瞬时或短时间释放；9个t相关参数，xy自己设
                allParam = tParam.myClone();
                allParam.setX(xParam.getX());
            } else {//6个x相关参数，4个t相关参数，y自己设
                allParam = xParam.myClone();
                allParam.setT(tParam.getT());
                allParam.setXc(tParam.getXc());
                allParam.setBx(tParam.getBx());
                allParam.setBetax(tParam.getBetax());
                allParam.setZ(tParam.getZ());
            }
            double y0 = 0;//设置Y轴起始值
            double y1 = this.stepY;//设置Y轴终点值
            allParam.setChange(0);//如果0点浓度过小，就不再计算
            double valueStart = allParam.getValue();
            if (valueStart > c[0]) {
                //第一圈浓度，c[0]
                double stepy = this.stepY;
                Map<String, Object> ndAndCoord = resultMap.get("nd1");
                List<double[]> pointList;
                if (null == ndAndCoord) {
                    ndAndCoord = new ConcurrentHashMap<>();
                    ndAndCoord.put("nd", c[0]);
                    pointList = new ArrayList<>();
                } else {
                    pointList = (List<double[]>) ndAndCoord.get("coord");
                }
                getConByColor(pointList, allParam, y0, y1, c[0], stepy);
                ndAndCoord.put("coord", pointList);
                resultMap.put("nd1", ndAndCoord);
            }
            if (valueStart > c[1]) {
                //第二圈浓度，c[1]
                double stepy = this.stepY;
                Map<String, Object> ndAndCoord = resultMap.get("nd2");
                List<double[]> pointList;
                if (null == ndAndCoord) {
                    ndAndCoord = new ConcurrentHashMap<>();
                    ndAndCoord.put("nd", c[1]);
                    pointList = new ArrayList<>();
                } else {
                    pointList = (List<double[]>) ndAndCoord.get("coord");
                }
                getConByColor(pointList, allParam, y0, y1, c[1], stepy);
                ndAndCoord.put("coord", pointList);
                resultMap.put("nd2", ndAndCoord);
            }
            if (valueStart > c[2]) {
                //第三圈浓度，c[2]
                double stepy = this.stepY;
                Map<String, Object> ndAndCoord = resultMap.get("nd3");
                List<double[]> pointList;
                if (null == ndAndCoord) {
                    ndAndCoord = new ConcurrentHashMap<>();
                    ndAndCoord.put("nd", c[2]);
                    pointList = new ArrayList<>();
                } else {
                    pointList = (List<double[]>) ndAndCoord.get("coord");
                }
                getConByColor(pointList, allParam, y0, y1, c[2], stepy);
                ndAndCoord.put("coord", pointList);
                resultMap.put("nd3", ndAndCoord);
            }
        }
        RunningStatus.deleteRunningTask();
    }

    public HeavyGasThread(List<HeavyGasModel> xList, HeavyGasModel tParam, double stepY, float[] c, int idspl, String userId) {
        if (CacheManager.get(userId + threadKey) == null) {
            CacheManager.getInstance().put(userId + threadKey, new ConcurrentHashMap<>());
        }
        this.idspl = idspl;
        this.xList = xList;
        this.tParam = tParam;
        this.stepY = stepY;
        this.c = c;
        this.userId = userId;
    }

    /**
     * 根据浓度，求x和y
     *
     * @param pointList [x,y,z]
     * @param allParam  计算参数
     * @param y0        y起始点
     * @param y1        y结束点
     * @param color     要求的浓度
     * @param stepy     y轴的步长
     */
    private void getConByColor(List<double[]> pointList, HeavyGasModel allParam, double y0, double y1, double color, double stepy) {
        double valueFirst;
        double valueLast = 100;
        double[] point = new double[2];
        double[] minusPoint = new double[2];//x的轴对称点，y的值为负
        while (Math.abs(valueLast - color) > color * 0.1) {
            allParam.setChange(y0);
            valueFirst = allParam.getValue();
            allParam.setChange(y1);
            valueLast = allParam.getValue();
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
            }
        }
        point[0] = allParam.getX();//x坐标
        point[1] = y1;//y坐标
        minusPoint[0] = allParam.getX();//x坐标
        minusPoint[1] = -y1;//y坐标
//        point[2] = allParam.getZ();//z坐标
//        System.out.println("Z轴值： " + allParam.getZ() + " 坐标： " + point);
        pointList.add(point);//xy坐标集合
        pointList.add(minusPoint);//x的轴对称点
    }

}
