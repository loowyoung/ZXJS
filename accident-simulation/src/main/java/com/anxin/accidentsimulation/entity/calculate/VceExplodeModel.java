package com.anxin.accidentsimulation.entity.calculate;

import lombok.Data;

/**
 * VCE蒸汽云爆炸
 *
 * @author: ly
 * @date: 2020/1/9 8:15
 */
@Data
public class VceExplodeModel implements BaseMatrix {
    private double p;//环境压力，一般取101325Pa
    private double bombR;//目标到爆炸源的水平距离，m
    private double bombN;//爆炸效率系数（0.04-0.65 之间）
    private double bombHeight;//爆炸距地面高度，近地面1，高空2
    private double bombW;//含能材料的质量，kg
    private double hc;//爆炸燃料的热量，kJ/kg

    @Override
    public void setChange(double change) {
        setBombR(change);
    }

    @Override
    public double getValue() {
        double Wtnt = this.bombN * this.bombW * this.hc / 4686;//nWQ
//        System.out.println("TNT当量：" + Wtnt);
        double Z = getBombR() / Math.pow(Wtnt, 1.0 / 3);
//        System.out.println("无量纲距离Z：" + Z);
        double deltaPs = getP() * getBombHeight() * 808 * (1 + Math.pow(Z / 4.5, 2)) /
                (Math.sqrt(1 + Math.pow(Z / 0.048, 2)) * Math.sqrt(1 + Math.pow(Z / 0.32, 2)) * Math.sqrt(1 + Math.pow(Z / 1.35, 2)));
        return deltaPs;
    }

    public double getP() {
        return p / 1000;//单位转为kPa
    }
}
