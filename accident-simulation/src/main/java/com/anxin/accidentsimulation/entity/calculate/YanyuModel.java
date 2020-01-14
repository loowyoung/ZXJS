package com.anxin.accidentsimulation.entity.calculate;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 泄漏：高斯烟羽模型
 *
 * @author: ly
 * @date: 2020/1/9 11:36
 */
@Data
public class YanyuModel extends LeakageModel {
    private float wms;//摩尔质量
    private String con;//乡村、城市
    private String stab;//大气稳定度
    private float qs;//泄漏速率
    private float ua;//风速
    private float hs;//有效源高度/距地面高度
    private float ta;//环境温度
    private float p;//环境压强

    public double getC() {
        double C, tmp1, tmp2, tmp3, tmp4, deltay = 0, deltaz = 0;
        switch (this.con) {
            case "乡村":
                double consY = getX() * Math.pow(1 + 0.0001 * getX(), -0.5f);
                switch (this.stab) {
                    case "A":
                        deltay = 0.22f * consY;
                        deltaz = 0.2f * getX();
                        break;
                    case "B":
                        deltay = 0.16f * consY;
                        deltaz = 0.12f * getX();
                        break;
                    case "C":
                        deltay = 0.11f * consY;
                        deltaz = 0.08f * getX() * Math.pow(1 + 0.0002f * getX(), -0.5f);
                        break;
                    case "D":
                        deltay = 0.08f * consY;
                        deltaz = 0.06f * getX() * Math.pow(1 + 0.0015f * getX(), -0.5f);
                        break;
                    case "E":
                        deltay = 0.06f * consY;
                        deltaz = 0.03f * getX() * Math.pow(1 + 0.0003f * getX(), -1f);
                        break;
                    case "F":
                        deltay = 0.04f * consY;
                        deltaz = 0.016f * getX() * Math.pow(1 + 0.0003f * getX(), -1f);
                        break;
                    default:
                        deltay = 0;
                        deltaz = 0;
                        break;
                }
                break;
            case "城市":
                double consCityY = getX() * Math.pow(1 + 0.0004f * getX(), -0.5f);
                switch (this.stab) {
                    case "A":
                    case "B":
                        deltay = 0.32f * consCityY;
                        deltaz = 0.24f * getX() * Math.pow(1 + 0.0001f * getX(), 0.5f);
                        break;
                    case "C":
                        deltay = 0.22f * consCityY;
                        deltaz = 0.2f;
                        break;
                    case "D":
                        deltay = 0.16f * consCityY;
                        deltaz = 0.14f * getX() * Math.pow(1 + 0.0003f * getX(), -0.5f);
                        break;
                    case "E":
                    case "F":
                        deltay = 0.11f * consCityY;
                        deltaz = 0.08f * getX() * Math.pow(1 + 0.0015f * getX(), -0.5f);
                        break;
                    default:
                        deltay = 0;
                        deltaz = 0;
                        break;
                }
                break;
            default:
                deltay = 0;
                deltaz = 0;
                break;
        }
        tmp1 = this.qs / (2 * Math.PI * this.ua * deltay * deltaz);
        tmp2 = -(getY() * getY()) / (2 * deltay * deltay);
        tmp3 = -Math.pow(getZ() - this.hs, 2) / (2 * deltaz * deltaz);
        tmp4 = -Math.pow(getZ() + this.hs, 2) / (2 * deltaz * deltaz);
        C = tmp1 * Math.pow(Math.E, tmp2 * 1f) * (Math.pow(Math.E, tmp3 * 1f) + Math.pow(Math.E, tmp4 * 1f));
        C *= (22.4 / (this.wms * 1000)) * ((273 + this.ta) / 273) * (101325 / this.p);
        return C;
    }

    @Override
    public double getValue() {
        // TODO Auto-generated method stub
        return this.getC();
    }

    @Override
    public void setChange(double y) {
        // TODO Auto-generated method stub
        setY(y);
    }

    //克隆
    @Override
    public YanyuModel myClone() {
        YanyuModel model = new YanyuModel();
        BeanUtils.copyProperties(this, model);
        return model;
    }

}
