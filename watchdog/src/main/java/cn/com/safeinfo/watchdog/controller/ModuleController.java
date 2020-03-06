package cn.com.safeinfo.watchdog.controller;

import cn.com.safeinfo.watchdog.model.entity.ModuleModel;
import cn.com.safeinfo.watchdog.model.param.UpLoadJarParam;
import cn.com.safeinfo.watchdog.service.ModuleService;
import cn.com.safeinfo.watchdog.util.result.JsonResult;
import cn.com.safeinfo.watchdog.util.result.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
public class ModuleController {
    @Autowired
    private ModuleService moduleService;

    @ApiOperation(value = "查询服务", notes = "查询所有服务的信息")
    @PostMapping("/list")
    public JsonResult list() {
        List<ModuleModel> model = moduleService.list();
        return ResultUtil.success(model);
    }

    @ApiOperation(value = "上传服务", notes = "上传服务的jar包")
    @PostMapping("/upload")
    public JsonResult serviceUpload(@RequestParam("file") @ApiParam("jar包") MultipartFile file,
                                    UpLoadJarParam param) {
        return moduleService.saveModule(file, param);
    }

    @ApiOperation(value = "启动服务", notes = "根据模块名启动服务")
    @PostMapping("/start")
    public JsonResult serviceStart(@RequestParam("moduleName") @ApiParam("模块名") String moduleName) {
        return moduleService.startModule(moduleName);
    }

    @ApiOperation(value = "停止服务", notes = "根据模块名停止服务")
    @PostMapping("/stop")
    public JsonResult serviceStop(String moduleName) {
        return moduleService.stopModule(moduleName);
    }

    @ApiOperation(value = "重启服务", notes = "根据模块名重启服务")
    @PostMapping("/restart")
    public JsonResult serviceRestart(String moduleName) {
        return ResultUtil.success("测试");
    }

}
