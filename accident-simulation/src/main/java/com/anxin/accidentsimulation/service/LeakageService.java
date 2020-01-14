package com.anxin.accidentsimulation.service;

import com.anxin.accidentsimulation.util.common.CacheManager;
import com.anxin.accidentsimulation.util.common.DealSlabFile;
import com.anxin.accidentsimulation.entity.calculate.HeavyGasModel;
import com.anxin.accidentsimulation.entity.calculate.YantuanModel;
import com.anxin.accidentsimulation.entity.calculate.YanyuModel;
import com.anxin.accidentsimulation.entity.param.GaussianParam;
import com.anxin.accidentsimulation.entity.param.HeavyGasParam;
import com.anxin.accidentsimulation.entity.param.SlabExecParam;
import com.anxin.accidentsimulation.util.LeakageUtil;
import com.anxin.accidentsimulation.util.threadPool.HeavyGasThread;
import com.anxin.accidentsimulation.util.threadPool.TaskExcutePools;
import com.anxin.accidentsimulation.util.threadPool.GaussianThread;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ly
 * @date: 2020/1/7 15:14
 */
@Service
public class LeakageService {

    /**
     * 算法的预处理，不进行计算只调用SLAB.exe程序；
     * 并将SLAB的结果返回;
     * slab.exe运行的结果是重气计算的参数
     */
    public Map<String, Object> getSlabParams(List<String> readParams, String userId) {
        //清除指定缓存
        CacheManager.getInstance().getCache().remove(userId + DealSlabFile.timpMapKey);
        if ("4".equals(readParams.get(0))) {
            DealSlabFile.cachePredict(readParams, "betax(t)", "1", 2, userId);
        } else {
            DealSlabFile.cachePredict(readParams, "betax(t)", "1", 1, userId);
        }
        Map<String, Object> timeInfoList = (Map<String, Object>) CacheManager.get(userId + DealSlabFile.timpMapKey);
        //返回数据
        return timeInfoList;
    }

    /**
     * 重气扩散计算
     *
     * @param param
     */
    public Map<String, Map> heavyGasCalculate(HeavyGasParam param) {
        String userId = param.getUserId();
        Map<String, Map> allPoints;//返回三个浓度圈点坐标
        //前台传过来的所有参数，包括物质属性、环境、场参数
        SlabExecParam slabExecParam = (SlabExecParam) CacheManager.getInstance().get(userId + LeakageUtil.inputParamKay);
        //获取Slab.exe运行结果
        Map<String, Object> slabExecResult = (Map<String, Object>) CacheManager.get(userId + DealSlabFile.timpMapKey);
        if (null == slabExecResult || slabExecResult.size() <= 0) {//如果slab.exe的运行结果为空
            allPoints = new HashMap<>();
            allPoints.put("请先运行Slab.exe", null);
            return allPoints;
        }
        HeavyGasModel modelParam = (HeavyGasModel) slabExecResult.get(param.getT());//获取该时间t下的参数
        if (null == modelParam) {//如果没有这个时间点
            allPoints = new HashMap<>();
            allPoints.put("无此时间点", null);
            return allPoints;
        }
        modelParam.setZ(param.getZ());//设置Z轴高度
        //slab.exe运行所有原始数据
        List<HeavyGasModel> slabAllParam = (List<HeavyGasModel>) CacheManager.get(userId + DealSlabFile.slabAllKey);//x坐标点集合，及x相关参数
        //清除指定缓存
        CacheManager.getInstance().getCache().remove(userId + HeavyGasThread.threadKey);
        float[] nongdu = new float[3];
        nongdu[0] = param.getOne();
        nongdu[1] = param.getTwo();
        nongdu[2] = param.getThree();
        long time1 = System.currentTimeMillis();
        //通过泄露源排放类型判断是用t为基准的计算还是用x为基准的判断
        if (4 == slabExecParam.getIdspl()) {
            //对应的t下y为0时,x最大值
            int maxX = LeakageUtil.getMaxX(modelParam);
            //对应的t下y为0时,x起始值
            int minX = LeakageUtil.getMinX(modelParam);
            //所需计算的x集合
            slabAllParam = LeakageUtil.getXList(minX, maxX, slabExecParam.getNcalc());//input4时，slab不产生X点集合，需要自己设
            //添加计算任务
            TaskExcutePools.addHeavyGasTask(slabAllParam, modelParam, 20, nongdu, 4, userId);
        } else {
            //添加计算任务
            TaskExcutePools.addHeavyGasTask(slabAllParam, modelParam, 20, nongdu, 3, userId);
        }
        allPoints = (Map<String, Map>) TaskExcutePools.getStateResult().get(userId + HeavyGasThread.threadKey);
        long time2 = System.currentTimeMillis();
        System.out.println("计算耗时：" + (time2 - time1) + "ms");
        LeakageUtil.pointDeal(allPoints, param, slabExecParam);
        //返回数据
        return allPoints;
    }

    /**
     * 高斯气体扩散逻辑处理
     *
     * @param param
     * @return
     */
    public Map<String, Map> gaussianCalculate(GaussianParam param) {
        long time1 = System.currentTimeMillis();
        Map<String, Map> result;
        if (param.getTsd() > 0) {//持续泄漏，烟羽
            result = yanyuCalculate(param);
        } else {//瞬时泄漏，烟团
            result = yantuanCalculate(param);
        }
        long time2 = System.currentTimeMillis();
        System.out.println("计算耗时：" + (time2 - time1) + "ms");
        return result;
    }


    /**
     * 高斯烟羽泄漏计算
     *
     * @param param
     * @return
     */
    private Map<String, Map> yanyuCalculate(GaussianParam param) {
        YanyuModel model = new YanyuModel();
        BeanUtils.copyProperties(param, model);
        //清除指定缓存
        CacheManager.getInstance().getCache().remove(param.getUserId() + GaussianThread.threadKey);
        float[] nongdu = new float[3];
        nongdu[0] = param.getOne();
        nongdu[1] = param.getTwo();
        nongdu[2] = param.getThree();
        TaskExcutePools.addGaussianTask(model, 0.1f, 100, param.getEndx(), nongdu, param.getUserId());
        Map<String, Map> allPoints = (Map<String, Map>) TaskExcutePools.getStateResult().get(param.getUserId() + GaussianThread.threadKey);
        return allPoints;
    }

    /**
     * 高斯烟团泄漏计算
     *
     * @param param
     * @return
     */
    private Map<String, Map> yantuanCalculate(GaussianParam param) {
        YantuanModel model = new YantuanModel();
        BeanUtils.copyProperties(param, model);
        //清除指定缓存
        CacheManager.getInstance().getCache().remove(param.getUserId() + GaussianThread.threadKey);
        float[] nongdu = new float[3];
        nongdu[0] = param.getOne();
        nongdu[1] = param.getTwo();
        nongdu[2] = param.getThree();
        TaskExcutePools.addGaussianTask(model, 0.1f, 100, param.getEndx(), nongdu, param.getUserId());
        Map<String, Map> allPoints = (Map<String, Map>) TaskExcutePools.getStateResult().get(param.getUserId() + GaussianThread.threadKey);
        return allPoints;
    }
}
