package com.anxin.jisuan.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: ly
 * @date: 2019/12/4 8:27
 */
@Data
@ApiModel(value = "计算模型")
public class CountParam {
    @ApiModelProperty(value = "参数xc", example = "11")
    private double xc;
    @ApiModelProperty(value = "参数bx", example = "22")
    private double bx;
    @ApiModelProperty(value = "参数betax", example = "33")
    private double betax;
}
