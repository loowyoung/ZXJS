package com.anxin.jisuan.model;

import lombok.Data;

/**
 * @author: ly
 * @date: 2018/12/10 10:16
 */
@Data
public class CountVo {
    private double x;
    private double y;
    private double nd;

    public CountVo(double x, double y, double nd) {
        this.x = x;
        this.y = y;
        this.nd = nd;
    }
}
