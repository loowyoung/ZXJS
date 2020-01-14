package com.anxin.accidentsimulation.entity.calculate;

import lombok.Data;

/**
 *  多线程计算的接口类
 * @author: ly
 * @date: 2020/1/8 11:03
 *
 */
public interface BaseMatrix {
    //设置变化值
    public void setChange(double change);
    //获取计算浓度
    public double getValue();
}
