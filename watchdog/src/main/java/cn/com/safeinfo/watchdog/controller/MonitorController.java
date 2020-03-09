package cn.com.safeinfo.watchdog.controller;

import cn.com.safeinfo.watchdog.common.result.JsonResult;
import cn.com.safeinfo.watchdog.common.result.ResultUtil;
import cn.com.safeinfo.watchdog.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 系统信息获取，包括服务器和服务
 *
 * @author: ly
 * @date: 2020/3/5 11:28
 */
@RestController
@Api(tags = "2.0 系统信息接口")
@RequestMapping("/systeminfo")
public class MonitorController {
    @Autowired
    MonitorService monitorService;

    @ApiOperation(value = "服务器使用信息", notes = "包含服务器的CPU使用率、内存使用率、磁盘使用率")
    @PostMapping("/server")
    public JsonResult serverCpuUsage() {
        Map result = monitorService.getserverInfo();
        return ResultUtil.success(result);
    }
//todo: 单个服务资源使用率接口
}
