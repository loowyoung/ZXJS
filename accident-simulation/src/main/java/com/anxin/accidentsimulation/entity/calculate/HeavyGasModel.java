package com.anxin.accidentsimulation.entity.calculate;

import com.anxin.accidentsimulation.util.LeakageUtil;
import lombok.Data;

/**
 * 重气泄漏计算模型
 * @author: ly
 * @date: 2020/1/8 10:59
 */
@Data
public class HeavyGasModel extends LeakageModel {
    private double sqrt2 = 1.4142135623730950488016887242097;

    private double cc;
    private double b;
    private double betac;
    private double zc;
    private double sig;
    private double t;
    private double xc;
    private double bx;
    private double betax;

    /**
     * 获取值
     *
     * @return
     */
    @Override
    public double getValue() {
        return this.cc * (LeakageUtil.erf(getXa()) - LeakageUtil.erf(getXb())) * (LeakageUtil.erf(getYa()) - LeakageUtil.erf(getYb())) * (Math.exp(-getZa() * getZa()) + Math.exp((-getZb() * getZb())));
    }

    @Override
    public void setChange(double y) {
        setY(y);
    }

    //克隆
    @Override
    public HeavyGasModel myClone() {
        HeavyGasModel leakageModel = new HeavyGasModel();
        leakageModel.setX(getX());
        leakageModel.setZ(getZ());
        leakageModel.setY(getY());
        leakageModel.setCc(this.cc);
        leakageModel.setB(this.b);
        leakageModel.setBetac(this.betac);
        leakageModel.setBetax(this.betax);
        leakageModel.setZc(this.zc);
        leakageModel.setSig(this.sig);
        leakageModel.setT(this.t);
        leakageModel.setXc(this.xc);
        leakageModel.setBx(this.bx);
        return leakageModel;
    }

    /**
     * 获取Xa
     *
     * @return
     */
    public double getXa() {
        return (getX() - this.xc + this.bx) / (this.sqrt2 * this.betax);
    }

    /**
     * 获取Xb
     *
     * @return
     */
    public double getXb() {
        return (getX() - this.xc - this.bx) / (this.sqrt2 * this.betax);
    }

    /**
     * 获取Ya
     *
     * @return
     */
    public double getYa() {
        return (getY() + this.b) / (this.sqrt2 * this.betac);
    }

    /**
     * 获取Yb
     *
     * @return
     */
    public double getYb() {
        return (getY() - this.b) / (this.sqrt2 * this.betac);
    }

    /**
     * 获取Za
     *
     * @return
     */
    public double getZa() {
        return (getZ() - this.zc) / (this.sqrt2 * this.sig);
    }

    /**
     * 获取Zb
     *
     * @return
     */
    public double getZb() {
        return (getZ() + this.zc) / (this.sqrt2 * this.sig);
    }

}
