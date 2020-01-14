package com.anxin.accidentsimulation.util.common;

import com.anxin.accidentsimulation.entity.calculate.HeavyGasModel;
import com.anxin.accidentsimulation.util.LeakageUtil;
import com.anxin.accidentsimulation.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行slab程序
 * 读取predict文件里面数据
 *
 * @author zl_xu
 */
@Component
public class DealSlabFile {
    //slab.exe运行所有原始数据
    public final static String slabAllKey = "SlabExecResultAll";
    //slab.exe运行每个时间点数据
    public final static String timpMapKey = "SlabExecResultMap";
    //slab.exe文件存储路径
    private static String slabPath;

    @Value(value = "${slab.path}")
    private void setSlabPath(String slabPath) {
        this.slabPath = slabPath;
    }


    /**
     * 同步执行slab将数据放入缓存
     *
     * @param params
     * @param start
     * @param end
     * @param type   1为烟羽，2为烟团
     * @param userId
     */
    public static synchronized void cachePredict(List<String> params, String start, String end, int type, String userId) {
        try {
            //执行slab
            if (runSlabExe(slabPath, params)) {
                //截取数据，按照规则存放
                transForm(getPredictData(slabPath, start, end), type, userId);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * 生成input文件，执行slab程序
     *
     * @param inputPath
     * @param params
     * @return
     */
    public static boolean runSlabExe(String inputPath, List<String> params) {
        File iptFile = new File(inputPath + File.separator + "input");
        //input文件里面写入参数
        FileUtil.write(iptFile, params);
        //判断结果文件是否已经存在
        File prtFile = new File(inputPath + File.separator + "predict");
        if (prtFile.exists()) {
            prtFile.delete();
        }
        Runtime rn = Runtime.getRuntime();
        int success = -1;
        //执行slab程序
        try {
            Process p = rn.exec(inputPath + File.separator + "SLAB.exe", null, new File(inputPath));
            success = p.waitFor();
        } catch (Exception e) {
            System.out.println("执行slab程序异常!");
            e.printStackTrace();
        }
        return (success < 0) ? false : true;
    }

    /**
     * 通过路径、开始节点、结束节点获取数据.
     *
     * @param inputPath
     * @param start
     * @param end
     * @return targetList
     */
    public static List<String> getPredictData(String inputPath, String start, String end) {
        File prtFile = new File(inputPath + File.separator + "predict");
        if (!prtFile.exists()) {
            return null;
        }
        //读取计算后的值
        List<String> valueLines = FileUtil.read(prtFile);
        List<String> targetList = new ArrayList<>();
        int index = 0;
        String temp;
        while (index < valueLines.size()) {
            temp = valueLines.get(index);
            if (temp != null && temp.trim().contains(start)) {
                for (int i = index + 1; i < valueLines.size(); i++) {
                    temp = valueLines.get(i);
                    //如果不传结束点则认为没有结束点
                    if (end != null && !"".equals(end)) {
                        if ("1".equals(temp.trim())) {
                            index = i = valueLines.size();
                            continue;
                        }
                    }
                    targetList.add(temp);
                }
            }
            index++;
        }
        return targetList;
    }

    /**
     * 将截取结果转化为predict对象集合
     *
     * @param targetList
     * @param type
     * @return predictList
     */
    public static void transForm(List<String> targetList, int type, String userId) {
        List<HeavyGasModel> dtoList = new ArrayList<>();
        switch (type) {
            //以x为单位的计算
            case 1:
                for (String temp : targetList) {
                    HeavyGasModel dto = new HeavyGasModel();
                    //空格分隔符分割
                    String[] valueArr = temp.split("\\s+");
                    //将数据放进对象中。
                    dto.setT(strToDouble(valueArr[6]));
                    dto.setXc(strToDouble(valueArr[7]));
                    dto.setBx(strToDouble(valueArr[8]));
                    dto.setBetax(strToDouble(valueArr[9]));
                    dto.setX(strToDouble(valueArr[0]));
                    dto.setCc(strToDouble(valueArr[1]));
                    dto.setB(strToDouble(valueArr[2]));
                    dto.setBetac(strToDouble(valueArr[3]));
                    dto.setZc(strToDouble(valueArr[4]));
                    dto.setSig(strToDouble(valueArr[5]));
                    dtoList.add(dto);
                }
                break;
            //以t为单位的计算
            case 2:
                for (String temp : targetList) {
                    HeavyGasModel dto = new HeavyGasModel();
                    //空格分隔符分割
                    String[] valueArr = temp.split("\\s+");
                    //将数据放进对象中。
                    dto.setT(strToDouble(valueArr[0]));
                    dto.setCc(strToDouble(valueArr[1]));
                    dto.setB(strToDouble(valueArr[2]));
                    dto.setBetac(strToDouble(valueArr[3]));
                    dto.setZc(strToDouble(valueArr[4]));
                    dto.setSig(strToDouble(valueArr[5]));
                    dto.setXc(strToDouble(valueArr[6]));
                    dto.setBx(strToDouble(valueArr[7]));
                    dto.setBetax(strToDouble(valueArr[8]));
                    dtoList.add(dto);
                }
                break;

        }
        //创建map集合用于存储时间
        Map<String, Object> timeMap = new HashMap<>();
        //遍历集合，将数据放进map里面
        for (int i = 0; i < dtoList.size(); i++) {
            String t = LeakageUtil.removePoint(dtoList.get(i).getT());
            //声明保存时间数据的对象t
            timeMap.put(t, dtoList.get(i));
        }
        CacheManager.getInstance().put(userId + slabAllKey, dtoList);//slab.exe运行所有原始数据
        CacheManager.getInstance().put(userId + timpMapKey, timeMap);//slab.exe运行每个时间点数据
    }

    /**
     * String转化成Double
     */
    public static double strToDouble(String str) {
        if (str == null || "".equals(str)) {
            return 0.0;
        }
        return Double.valueOf(str);
    }
}
