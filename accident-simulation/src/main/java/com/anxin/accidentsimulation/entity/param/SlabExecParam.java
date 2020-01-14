package com.anxin.accidentsimulation.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: ly
 * @date: 2020/1/7 16:14
 * Slab.exe执行所需参数
 */
@Data
@ApiModel(value = "Slab.exe执行所需参数")
public class SlabExecParam {
    //1蒸发池释放；2水平；3垂直；4瞬时或短时间
    @ApiModelProperty(value = "泄漏类型，1蒸发池释放；2水平；3垂直；4瞬时或短时间", example = "3")
    private int idspl;
    @ApiModelProperty(value = "摩尔质量(kg/mol)", example = "0.070906")
    private String wms;
    @ApiModelProperty(value = "定压比热(j/kg•k)", example = "498.1")
    private String cps;
    @ApiModelProperty(value = "沸点(k)", example = "239.1")
    private String tbp;
    @ApiModelProperty(value = "质量分数（0-1）", example = "0.88")
    private String cmedo;
    @ApiModelProperty(value = "蒸发热(j/kg)", example = "287840")
    private String dhe;
    @ApiModelProperty(value = "液相比热(j/kg•k)", example = "926.3")
    private String cpsl;
    @ApiModelProperty(value = "液态时密度(kg/m³)", example = "1574")
    private String rhosl;
    @ApiModelProperty(value = "饱和压力常数(SPB)", example = "1978.34")
    private String spb;
    @ApiModelProperty(value = "饱和压力常数(SPC)", example = "-27.01")
    private String spc;

    @ApiModelProperty(value = "物质温度(℃)", example = "239.1")
    private String ts;
    @ApiModelProperty(value = "泄漏速率(kg/s)", example = "3.33")
    private String qs;
    @ApiModelProperty(value = "泄漏口面积(m²)", example = "0.02")
    private String as;
    @ApiModelProperty(value = "泄漏持续时间(s)", example = "300")
    private String tsd;
    @ApiModelProperty(value = "瞬时泄漏量(kg)", example = "0")
    private String qtis;
    @ApiModelProperty(value = "距地面高度(m)", example = "1")
    private String hs;

    @ApiModelProperty(value = "浓度平均时间(s)", example = "1")
    private String tav;
    @ApiModelProperty(value = "场大小(下风向最远距离)(m)", example = "3000")
    private String xffm;
    @ApiModelProperty(value = "计算步长", example = "1")
    private String ncalc;

    @ApiModelProperty(value = "地面粗糙度(m)", example = "0.1")
    private String zo;
    @ApiModelProperty(value = "环境测量高度(m):", example = "10")
    private String za;
    @ApiModelProperty(value = "风速(m/s)", example = "2")
    private String ua;
    @ApiModelProperty(value = "环境温度(℃)", example = "20")
    private String ta;
    @ApiModelProperty(value = "相对湿度(%)", example = "30")
    private String rh;
    @ApiModelProperty(value = "大气稳定度(A-F)", example = "D")
    private String stab;

    @ApiModelProperty(value = "下风向水平截面高度(1)", example = "0")
    private String zp1;
    @ApiModelProperty(value = "下风向水平截面高度(2)", example = "0")
    private String zp2;
    @ApiModelProperty(value = "下风向水平截面高度(3)", example = "0")
    private String zp3;
    @ApiModelProperty(value = "下风向水平截面高度(4)", example = "0")
    private String zp4;

    @ApiModelProperty(value = "用户ID", example = "007")
    private String userId;

    public void setTs(String ts) {
        this.ts = String.valueOf(Double.valueOf(ts) + 273);//转换为开氏度
    }

    public void setTa(String ta) {
        this.ta = String.valueOf(Double.valueOf(ta) + 273);//转换为开氏度
    }

    public void setStab(String stab) {
        switch (stab) {
            case "A":
                this.stab = "1";
                break;
            case "B":
                this.stab = "2";
                break;
            case "C":
                this.stab = "3";
                break;
            case "D":
                this.stab = "4";
                break;
            case "E":
                this.stab = "5";
                break;
            case "F":
                this.stab = "6";
                break;
            default:
                break;
        }
    }

}
