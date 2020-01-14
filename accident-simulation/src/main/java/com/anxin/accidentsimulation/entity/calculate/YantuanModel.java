package com.anxin.accidentsimulation.entity.calculate;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 泄漏：高斯烟团模型
 *
 * @author: ly
 * @date: 2020/1/9 11:36
 */
@Data
public class YantuanModel extends LeakageModel implements BaseMatrix {
    private float wms;//摩尔质量
    private float t;
    private float qtis;//原来泄漏量 = tsd * qs，现在改为瞬时泄露量
    private String stab;
    private float ua;//风速
    private float hs;//有效源高度/距地面高度
    private float ta;//环境温度
    private float p;

    @Override
    public void setChange(double change) {
        setY(change);
    }

    /**
     * 烟团浓度值计算
     *
     * @return
     */
    @Override
    public double getValue() {
        double deltaX = 0, deltaY = 0, deltaZ = 0;
        switch (this.stab) {
            case "A":
                deltaX = 0.18 * Math.pow(getX(), 0.92);
                deltaY = 0.18 * Math.pow(getX(), 0.92);
                deltaZ = 0.6 * Math.pow(getX(), 0.75);
                break;
            case "B":
                deltaX = 0.14 * Math.pow(getX(), 0.92);
                deltaY = 0.14 * Math.pow(getX(), 0.92);
                deltaZ = 0.53 * Math.pow(getX(), 0.73);
                break;
            case "C":
                deltaX = 0.1 * Math.pow(getX(), 0.92);
                deltaY = 0.1 * Math.pow(getX(), 0.92);
                deltaZ = 0.34 * Math.pow(getX(), 0.71);
                break;
            case "D":
                deltaX = 0.06 * Math.pow(getX(), 0.92);
                deltaY = 0.06 * Math.pow(getX(), 0.92);
                deltaZ = 0.15 * Math.pow(getX(), 0.7);
                break;
            case "E":
                deltaX = 0.04 * Math.pow(getX(), 0.92);
                deltaY = 0.04 * Math.pow(getX(), 0.92);
                deltaZ = 0.1 * Math.pow(getX(), 0.65);
                break;
            case "F":
                deltaX = 0.02 * Math.pow(getX(), 0.89);
                deltaY = 0.02 * Math.pow(getX(), 0.89);
                deltaZ = 0.05 * Math.pow(getX(), 0.61);
                break;
            default:
                break;
        }
        double tmp1 = this.qtis / (Math.pow(2 * Math.PI, 1.5) * deltaX * deltaY * deltaZ);
        double tmp2 = -(getY() * getY()) / (2 * deltaZ * deltaZ);//deltaY修改为deltaZ，赵桂利参考国外资料修改
        double tmp3 = -Math.pow(getZ() - this.hs, 2) / (2 * deltaZ * deltaZ);
        double tmp4 = -Math.pow(getZ() + this.hs, 2) / (2 * deltaZ * deltaZ);
        double tmp5 = -Math.pow(getX() - this.ua * this.t, 2) / (2 * deltaX * deltaX);
        double a = tmp1 * Math.pow(Math.E, tmp2 * 1);
        double b = (Math.pow(Math.E, tmp3 * 1) + Math.pow(Math.E, tmp4 * 1));
        double c = Math.pow(Math.E, tmp5 * 1);
        double conver = (22.4 / (this.wms * 1000)) * ((273 + this.ta) / 273) * (101325 / this.p);
        return conver * a * b * c;
    }

    //克隆
    @Override
    public YantuanModel myClone() {
        YantuanModel yantuanModel = new YantuanModel();
        BeanUtils.copyProperties(this, yantuanModel);
        return yantuanModel;
    }

}
