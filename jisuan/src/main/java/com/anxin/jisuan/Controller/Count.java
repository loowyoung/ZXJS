package com.anxin.jisuan.Controller;


import com.anxin.jisuan.common.Equation;
import com.anxin.jisuan.model.CountVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 计算结果
 *
 * @author: ly
 * @date: 2018/12/7 14:52
 */
public class Count {

    public List<CountVo> sum(String[][] arr,double xc,double bx,double betax) {

        List<CountVo> resultList = new ArrayList<>();
//        double xc = Double.valueOf(5.27E+01);
//        double bx = Double.valueOf(5.10E+01);
//        double betax = Double.valueOf(4.16E-01);
        double z = Double.valueOf(0);
        Equation equation = new Equation();
        for (int i = 0; i < arr.length; i++) {
            if (null != arr[i][2] && arr[i][2].indexOf("E+") > 0) {
//                for (double y = -200; y < 200; y += 1) {
                for (double y = -300; y < 300; y += 0.1) {
                    double x = Double.valueOf(arr[i][2]);
                    double cc = Double.valueOf(arr[i][4]);
                    double b = Double.valueOf(arr[i][6]);
                    double betac = Double.valueOf(arr[i][8]);
                    double zc = Double.valueOf(arr[i][10]);
                    double sig = Double.valueOf(arr[i][12]);
                    double result = equation.method(x, cc, b, betac, zc, sig, xc, bx, betax, y, z);
                    //大于-1E-11； 0是等于，-1是小于，1是大于
                    if (result == 0) {
                        continue;
                    }
                    CountVo countVo = new CountVo(x, y, result);
                    resultList.add(countVo);
                }
            }
        }
        return resultList;
    }

}
