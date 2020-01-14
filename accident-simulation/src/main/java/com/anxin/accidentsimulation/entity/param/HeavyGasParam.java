package com.anxin.accidentsimulation.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: ly
 * @date: 2020/1/8 10:51
 * 重气泄漏计算所需参数
 */
@Data
@ApiModel(value = "重气泄漏所需参数")
public class HeavyGasParam {
    @ApiModelProperty(value = "泄漏时间", example = "由接口/leakage/getParam获得")
    private String t;
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
