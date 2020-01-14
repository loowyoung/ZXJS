package com.anxin.accidentsimulation.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 高斯泄漏计算所需参数
 *
 * @author: ly
 * @date: 2020/1/9 11:10
 */
@ApiModel(value = "高斯泄漏参数")
@Data
public class GaussianParam {
    @ApiModelProperty(value = "摩尔质量(kg/mol)", example = "0.070906")
    private float wms;//摩尔质量
    @ApiModelProperty(value = "泄漏时间(s)", example = "10")
    private float t;//（烟团）
    @ApiModelProperty(value = "瞬时泄露量(kg)", example = "6000")
    private float qtis;//瞬时泄露量（烟团）
    @ApiModelProperty(value = "乡村、城市", example = "城市")
    private String con;//乡村、城市（烟羽）
    @ApiModelProperty(value = "大气稳定度", example = "D")
    private String stab;//大气稳定度
    @ApiModelProperty(value = "泄漏速率(kg/s)", example = "1000")
    private float qs;//泄漏速率（烟羽）
    @ApiModelProperty(value = "风速(m/s)", example = "2")
    private float ua;//风速
    @ApiModelProperty(value = "距地面高度(m)", example = "0")
    private float hs;//有效源高度/距地面高度（烟羽）
    @ApiModelProperty(value = "环境温度(℃)", example = "20")
    private float ta;//环境温度
    @ApiModelProperty(value = "环境压强(Pa)", example = "101325")
    private float p;//环境压强
    @ApiModelProperty(value = "下风向最远距离(m)", example = "3000")
    private float endx;//持续时间
    @ApiModelProperty(value = "持续时间(s)", example = "300")
    private float tsd;//持续时间（烟羽）
    @ApiModelProperty(value = "三个圈(ppm)：高浓度", example = "1000")
    private float one;
    @ApiModelProperty(value = "三个圈(ppm)：中浓度", example = "100")
    private float two;
    @ApiModelProperty(value = "三个圈(ppm)：低浓度", example = "10")
    private float three;
    @ApiModelProperty(value = "Z轴高度", example = "0")
    private double z;
    @ApiModelProperty(value = "用户ID", example = "007")
    private String userId;

    public void setOne(float one) {
        this.one = one * 0.000001f;//ppm
    }

    public void setTwo(float two) {
        this.two = two * 0.000001f;
    }

    public void setThree(float three) {
        this.three = three * 0.000001f;
    }
}
