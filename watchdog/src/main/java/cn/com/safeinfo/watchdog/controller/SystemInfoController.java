package cn.com.safeinfo.watchdog.controller;

import cn.com.safeinfo.watchdog.common.result.JsonResult;
import cn.com.safeinfo.watchdog.common.result.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统信息获取，包括服务器和服务
 *
 * @author: ly
 * @date: 2020/3/5 11:28
 */
@RestController
@Api(tags = "2.0 系统信息接口")
@RequestMapping("/systeminfo")
public class SystemInfoController {
    @ApiOperation(value = "服务器CPU使用率", notes = "获取服务器CPU使用率")
    @PostMapping("/serverCpuUsage")
    public JsonResult serverCpuUsage(String moduleName) {
        return ResultUtil.success("测试");
    }

    @ApiOperation(value = "服务CPU使用率", notes = "根据进程号，获取服务CPU使用率")
    @PostMapping("/serviceCpuUsage")
    public JsonResult serviceCpuUsage(String moduleName) {
        return ResultUtil.success("测试");
    }

    @ApiOperation(value = "服务器内存使用率", notes = "获取服务器内存使用率")
    @PostMapping("/serverMomoryUsage")
    public JsonResult serverMomoryUsage(String moduleName) {
        return ResultUtil.success("测试");
    }

    @ApiOperation(value = "服务内存使用率", notes = "根据进程号，获取服务内存使用率")
    @PostMapping("/serviceMomoryUsage")
    public JsonResult serviceMomoryUsage(String moduleName) {
        return ResultUtil.success("测试");
    }

    @ApiOperation(value = "服务器磁盘使用率", notes = "获取服务器磁盘使用率")
    @PostMapping("/serverDiskUsage")
    public JsonResult serverDiskUsage(String moduleName) {
        return ResultUtil.success("测试");
    }
}
