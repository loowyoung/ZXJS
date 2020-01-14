package com.anxin.accidentsimulation.entity.calculate;

import lombok.Data;

/**
 * 泄漏父类
 *
 * @author: ly
 * @date: 2020/1/9 14:45
 */
@Data
public class LeakageModel implements BaseMatrix {
    private double x;
    private double y;
    private double z;

    //设置变化值
    @Override
    public void setChange(double change) {
        System.out.println("设置变化值");
    }

    //获取计算浓度
    @Override
    public double getValue() {
        System.out.println("获取计算结果");
        return 0;
    }

    public LeakageModel myClone() {
        return null;
    }
}
