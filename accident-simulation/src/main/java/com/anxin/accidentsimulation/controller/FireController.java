package com.anxin.accidentsimulation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/fire")
@Api(tags = "4.火灾计算接口")
public class FireController {

    @ApiOperation(value = "火灾", notes = "输入参数，计算火灾影响范围")
    @PostMapping("/getCount")
    public Map<String, Map> getCount(@RequestBody String id) {
        return null;
    }

}

