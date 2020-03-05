package cn.com.safeinfo.watchdog.controller;

import cn.com.safeinfo.watchdog.model.entity.ModuleModel;
import cn.com.safeinfo.watchdog.model.vo.ModuleVo;
import cn.com.safeinfo.watchdog.service.JarService;
import cn.com.safeinfo.watchdog.service.ModuleService;
import cn.com.safeinfo.watchdog.util.result.JsonResult;
import cn.com.safeinfo.watchdog.util.result.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务管理
 *
 * @author: ly
 * @date: 2020/3/5 8:50
 */
@RestController
@Api(tags = "1.0 服务管理接口")
@RequestMapping("/service")
public class JarController {
    @Autowired
    JarService jarService;
    @Autowired
    private ModuleService moduleService;

    @ApiOperation(value = "启动服务", notes = "根据模块名启动服务")
    @PostMapping("/start")
    public JsonResult serviceStart(String moduleName) {
        jarService.runJar();
        return ResultUtil.success("测试");
    }

    @ApiOperation(value = "停止服务", notes = "根据模块名停止服务")
    @PostMapping("/stop")
    public JsonResult serviceStop(String moduleName) {
        jarService.stopJar(moduleName);
        return ResultUtil.success("测试");
    }

    @ApiOperation(value = "重启服务", notes = "根据模块名重启服务")
    @PostMapping("/restart")
    public JsonResult serviceRestart(String moduleName) {
        return ResultUtil.success("测试");
    }

    @ApiOperation(value = "上传服务", notes = "上传服务的jar包")
    @PostMapping("/upload")
    public JsonResult serviceUpload(String moduleName) {
        return ResultUtil.success("测试");
    }

    @ApiOperation(value = "查询服务", notes = "查询所有服务的信息")
    @PostMapping("/list")
    public JsonResult list(String moduleName) {
        List<ModuleModel> model = moduleService.list();
        List<ModuleVo> voList = new ArrayList<>();
        //遍历，类拷贝
        model.forEach(m -> {
            ModuleVo vo = new ModuleVo();
            BeanUtils.copyProperties(m, vo);
            voList.add(vo);
        });
        return ResultUtil.success(voList);
    }
}
