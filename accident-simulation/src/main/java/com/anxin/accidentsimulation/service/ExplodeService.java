package com.anxin.accidentsimulation.service;

import com.anxin.accidentsimulation.util.common.CacheManager;
import com.anxin.accidentsimulation.entity.calculate.VceExplodeModel;
import com.anxin.accidentsimulation.entity.param.VceExplodeParam;
import com.anxin.accidentsimulation.util.threadPool.TaskExcutePools;
import com.anxin.accidentsimulation.util.threadPool.VceExplodeThread;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 爆炸逻辑处理类
 *
 * @author: ly
 * @date: 2020/1/9 8:47
 */
@Service
public class ExplodeService {

    /**
     * 蒸气云爆炸XY等值面绘图（Vapor Cloud Explosion，简称VCE）
     */
    public Map<String, Map> VceCalculate(VceExplodeParam param) {
        //清除指定缓存
        CacheManager.getInstance().getCache().remove(param.getUserId() + VceExplodeThread.threadKey);
        float[] nongdu = new float[3];
        nongdu[0] = param.getOne();
        nongdu[1] = param.getTwo();
        nongdu[2] = param.getThree();
        VceExplodeModel model = new VceExplodeModel();
        BeanUtils.copyProperties(param, model);
        //循环添加任务计算
        TaskExcutePools.addVceTask(model, 0.1f, 1, 0.1f, nongdu, param.getUserId());
        //将数据转化为对象传到前台
        Map<String, Map> allPoints = (Map<String, Map>)TaskExcutePools.getStateResult().get(param.getUserId() + VceExplodeThread.threadKey);
        return allPoints;
    }
}
