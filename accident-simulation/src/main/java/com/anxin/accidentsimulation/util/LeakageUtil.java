package com.anxin.accidentsimulation.util;

import com.anxin.accidentsimulation.util.common.CacheManager;
import com.anxin.accidentsimulation.entity.calculate.HeavyGasModel;
import com.anxin.accidentsimulation.entity.param.HeavyGasParam;
import com.anxin.accidentsimulation.entity.param.SlabExecParam;
import org.apache.commons.math3.special.Erf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author: ly
 * @date: 2020/1/7 16:00
 */
public class LeakageUtil {
    //存放所有输入参数
    public static final String inputParamKay = "inputParamAll";

    /**
     * 小数点后边为0 则去除；
     * 将科学记数法转为普通数字
     */
    public static String removePoint(double num) {
        String aa;
        if (num % 1 == 0) {
            aa = String.valueOf(new Double(num).intValue());
        } else {
            aa = String.valueOf(num);
        }
        if (!"NaN".equals(aa)) {
            BigDecimal bd = new BigDecimal(aa);//将科学记数法转为普通数字
            aa = bd.toPlainString();
        }
        return aa;
    }

    /**
     * 执行Slab.exe所需的参数格式化
     * 从前台获取数据并且处理
     */
    public List<String> handleData(SlabExecParam param) {
        //清空输入的所有参数
        CacheManager.getInstance().getCache().remove(param.getUserId() + inputParamKay);
        //存放输入的所有参数
        CacheManager.getInstance().put(param.getUserId() + inputParamKay, param);
        //读取后存到input文件里面
        List<String> readParams = new ArrayList<>();
        //泄漏源排放类型
        readParams.add(param.getIdspl() + "");
        //计算步长
        readParams.add(param.getNcalc());
        //分子量
        readParams.add(addPoint(param.getWms()));
        //定压热容
        readParams.add(addPoint(param.getCps()));
        //沸点
        readParams.add(addPoint(param.getTbp()));
        //初始液体质量分数
        readParams.add(addPoint(param.getCmedo()));
        //蒸发热
        readParams.add(addPoint(param.getDhe()));
        //液态热容
        readParams.add(addPoint(param.getCpsl()));
        //液态时密度
        readParams.add(addPoint(param.getRhosl()));
        //饱和压力常数
        readParams.add(addPoint(param.getSpb()));
        //饱和压力常数
        readParams.add(addPoint(param.getSpc()));
        //温度 转换为开氏度
        readParams.add(addPoint(param.getTs()));
        //泄漏速率
        readParams.add(addPoint(param.getQs()));
        //泄漏口面积
        readParams.add(addPoint(param.getAs()));
        //持续时间
        readParams.add(addPoint(param.getTsd()));
        //瞬时泄漏量
        readParams.add(addPoint(param.getQtis()));
        //距地面高度
        readParams.add(addPoint(param.getHs()));
        //浓度平均时间
        readParams.add(addPoint(param.getTav()));
        //流场最大长度
        readParams.add(addPoint(param.getXffm()));
        //下风向水平截面高度(1)
        String zp1 = param.getZp1();
        if ("".equals(zp1) || null == zp1) {
            zp1 = "0";
        }
        readParams.add(addPoint(zp1));
        //下风向水平截面高度(2)
        String zp2 = param.getZp2();
        if ("".equals(zp2) || null == zp2) {
            zp2 = "0";
        }
        readParams.add(addPoint(zp2));
        //下风向水平截面高度(3)
        String zp3 = param.getZp3();
        if ("".equals(zp3) || null == zp3) {
            zp3 = "0";
        }
        readParams.add(addPoint(zp3));
        //下风向水平截面高度(4)
        String zp4 = param.getZp4();
        if ("".equals(zp4) || null == zp4) {
            zp4 = "0";
        }
        readParams.add(addPoint(zp4));
        //地面粗糙度
        readParams.add(addPoint(param.getZo()));
        //环境测量高度
        readParams.add(addPoint(param.getZa()));
        //风速
        readParams.add(addPoint(param.getUa()));
        //温度
        readParams.add(addPoint(param.getTa()));
        //相对湿度
        readParams.add(addPoint(param.getRh()));
        //大气稳定度值
        readParams.add(addPoint(param.getStab()));
        //最后结尾拼上"-1"
        String last = "-1";
        readParams.add(addPoint(last));
        return readParams;
    }

    /**
     * 没有点的添加点
     *
     * @param str
     * @return
     */
    public String addPoint(String str) {
        //首先判断是否有点号
        if (!str.contains(".")) {
            str = str + ".";
        }
        return str;
    }

    /**
     * 误差函数
     *
     * @param x
     * @return
     */
    public static double erf(double x) {
        return Erf.erf(0, x);
    }

    /**
     * 重气点处理
     *
     * @param pointList
     * @param param
     * @param slabExecParam
     * @return
     */
    public static void pointDeal(Map<String, Map> pointList, HeavyGasParam param, SlabExecParam slabExecParam) {
        //添加坐标原点
        adddZeroPoint(pointList.get("nd1"), param, slabExecParam);
        adddZeroPoint(pointList.get("nd2"), param, slabExecParam);
        adddZeroPoint(pointList.get("nd3"), param, slabExecParam);
//        removePointTest(pointList.get("nd1"));
//        removePointTest(pointList.get("nd2"));
//        removePointTest(pointList.get("nd3"));
    }

    /**
     * 在泄漏持续时间内，添加0点
     *
     * @param pointList
     * @param param
     * @param slabExecParam
     */
    public static void adddZeroPoint(Map<String, Map> pointList, HeavyGasParam param, SlabExecParam slabExecParam) {
        if (Float.parseFloat(param.getT()) <= Float.parseFloat(slabExecParam.getTsd())) {
            double[] point0 = new double[2];
            point0[0] = 0;
            point0[1] = 0;
            List<double[]> coordList = (List<double[]>) pointList.get("coord");
            coordList.add(point0);
        }
    }

    /**
     * 重气泄漏input4
     * 获取当前时间下x的最远点
     */
    public static int getMaxX(HeavyGasModel model) {
        //起始位置浓度为0，直至遇到浓度不为0
        boolean isXFirst = true;
        double value = 0;
        int x = 0;
        while (value > 0 || isXFirst) {
            //x值自增
            x += 10;
            //给计算对象赋值X轴的值
            model.setX(x);
            value = model.getValue();
            if (value > 0) {
                isXFirst = false;//直至遇到浓度不为0
            }
        }
        return x;
    }

    /**
     * 重气泄漏input4
     * 获取当前时间下x的起始点
     */
    public static int getMinX(HeavyGasModel model) {
        //起始位置浓度为0，直至遇到浓度不为0
        boolean isXFirst = true;
        double value = 0;
        int x = 0;
        while (value > 0 || isXFirst) {
            //x值自增
            x -= 10;
            //给计算对象赋值X轴的值
            model.setX(x);
            value = model.getValue();
            if (value > 0) {
                isXFirst = false;//直至遇到浓度不为0
            }
        }
        return x;
    }

    /**
     * 重气泄漏input4
     * 设置x的范围
     *
     * @param minX  x起始点
     * @param maxX  x结束点
     * @param ncalc x步长
     * @return
     */
    public static List<HeavyGasModel> getXList(int minX, int maxX, String ncalc) {
        float step = Float.valueOf(ncalc);
        List<HeavyGasModel> models = new ArrayList<>();
        for (double i = minX; i < maxX; i += step) {
            HeavyGasModel model = new HeavyGasModel();
            model.setX(i);
            models.add(model);
        }
        return models;
    }

    public static void removePointTest(Map pointList) {
        List<double[]> coordList = (List<double[]>) pointList.get("coord");
//        coordList.forEach(item -> {
//            if (item[0] < 2) {
//                coordList.remove(item);
//            }
//        });
//        Iterator<double[]> it = coordList.iterator();
//        while (it.hasNext()) {
//            double[] a = it.next();
//            if (a[0] < 10) {
//                it.remove();
//            }
//        }
    }

}
