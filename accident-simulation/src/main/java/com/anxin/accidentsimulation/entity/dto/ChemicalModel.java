package com.anxin.accidentsimulation.entity.dto;

import lombok.Data;

/**
 * 化学物质实体类
 *
 * @author: ly
 * @date: 2020/1/10 15:59
 */
@Data
public class ChemicalModel {
    //id
    private String systemId;
    //物质名称
    private String sourceName;
    //中文名
    private String sourceCname;
    //分子量(摩尔质量)
    private Double wms;
    //沸点
    private Double tbp;
    //定压热容(定压比热)
    private Double cps;
    //液态热熔(定容比热)
    private Double cpsl;
    //蒸发热
    private Double dhe;
    //燃烧热
    private Double hc;
    //饱和压力常数
    private Double spb;
    //饱和压力常数
    private Double spc;
    //液态时密度
    private Double rhosl;
    //气态时密度
    private Double rhos;
    //绝热指数
    private Double adiabaticIndex;
    //相对密度
    private String relativeDensity;
    //泄漏液体燃烧速度(kg/m2.s)
    private Double mf;
    //物质浓度标准 erpg、aegl、teel、 idlh
    private String erpg_1;
    private String erpg_2;
    private String erpg_3;
    private String aegl_1;
    private String aegl_2;
    private String aegl_3;
    private String teel_1;
    private String teel_2;
    private String teel_3;
    private String idlh;
}
