package com.anxin.accidentsimulation.controller;

import com.anxin.accidentsimulation.entity.param.VceExplodeParam;
import com.anxin.accidentsimulation.service.ExplodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: ly
 * @date: 2020/1/7 14:12
 */

@RestController
@RequestMapping("/explode")
@Api(tags = "3.爆炸计算接口")
public class ExplodeController {
    @Autowired
    private ExplodeService service;

    @ApiOperation(value = "Vce蒸汽云爆炸", notes = "使用TNT当量法")
    @PostMapping("/vceCalculate")
    public Map<String, Map> getCount(@RequestBody VceExplodeParam param) {
        Map<String, Map> result = service.VceCalculate(param);
        return result;
    }

}

