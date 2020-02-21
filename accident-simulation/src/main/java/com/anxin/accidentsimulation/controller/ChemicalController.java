package com.anxin.accidentsimulation.controller;

import com.anxin.accidentsimulation.entity.dto.ChemicalModel;
import com.anxin.accidentsimulation.service.ChemicalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 化学物质管理
 *
 * @author: ly
 * @date: 2020/1/10 11:40
 */
@RestController
@Api(tags = "1.化学物质管理")
@RequestMapping("/chemical")
public class ChemicalController {
    @Autowired
    private ChemicalService service;

    @ApiOperation(value = "查询所有", notes = "查询所有化学物质")
    @PostMapping("/list")
    public List<ChemicalModel> list() {
        return service.list("");
    }
}
