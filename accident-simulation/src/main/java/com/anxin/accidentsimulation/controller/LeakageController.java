package com.anxin.accidentsimulation.controller;

import com.anxin.accidentsimulation.entity.param.GaussianParam;
import com.anxin.accidentsimulation.entity.param.HeavyGasParam;
import com.anxin.accidentsimulation.entity.param.SlabExecParam;
import com.anxin.accidentsimulation.service.LeakageService;
import com.anxin.accidentsimulation.util.LeakageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: ly
 * @date: 2020/1/7 14:12
 * <p>
 * 使用用户id重命名slab结果文件
 */

@RestController
@RequestMapping("/leakage")
@Api(tags = "2.泄漏计算接口")
public class LeakageController {

    @Autowired
    private LeakageService leakageService;

    @ApiOperation(value = "Slab时间参数", notes = "获取重气泄漏所需的时间相关参数")
    @PostMapping("/getParam")
    public Map<String, Object> getParam(@RequestBody SlabExecParam param) {
        LeakageUtil util = new LeakageUtil();
        Map<String, Object> tMap = leakageService.getSlabParams(util.handleData(param), param.getUserId());
        return tMap;
    }

    @ApiOperation(value = "泄漏重气计算", notes = "计算某一时刻下的重气泄漏范围")
    @PostMapping("/heavyGasCalculate")
    public Map<String, Map> heavyGasCalculate(@RequestBody HeavyGasParam param) {
        Map<String, Map> result = leakageService.heavyGasCalculate(param);
        return result;
    }

    @ApiOperation(value = "泄漏高斯计算", notes = "计算某一时刻下的高斯气体泄漏范围")
    @PostMapping("/gaussianCalculate")
    public Map<String, Map> gaussianCalculate(@RequestBody GaussianParam param) {
        Map<String, Map> result = leakageService.gaussianCalculate(param);
        return result;
    }
}

