package com.anxin.accidentsimulation.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: ly
 * @date: 2020/1/9 8:40
 */
@Data
public class VceExplodeParam {
    @ApiModelProperty(value = "环境压力，一般取101325Pa", example = "101325")
    private double p;//环境压力，一般取101325Pa
    @ApiModelProperty(value = "爆炸效率系数（0.04-0.65 之间）", example = "0.04")
    private double bombN;//爆炸效率系数（0.04-0.65 之间）
    @ApiModelProperty(value = "爆炸距地面高度，近地面1，高空2", example = "2")
    private double bombHeight;//爆炸距地面高度，近地面1，高空2
    @ApiModelProperty(value = "材料的质量(kg)", example = "1000")
    private double bombW;//含能材料的质量，kg
    @ApiModelProperty(value = "爆炸燃料的热量(kJ/kg)", example = "51168.75")
    private double hc;//爆炸燃料的热量，kJ/kg
    @ApiModelProperty(value = "三个圈(kPa)：高超压", example = "70")
    private float one;
    @ApiModelProperty(value = "三个圈(kPa)：中超压", example = "50")
    private float two;
    @ApiModelProperty(value = "三个圈(kPa)：低超压", example = "10")
    private float three;
    @ApiModelProperty(value = "用户ID", example = "007")
    private String userId;
}
