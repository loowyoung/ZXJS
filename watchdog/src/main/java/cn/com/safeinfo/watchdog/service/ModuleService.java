package cn.com.safeinfo.watchdog.service;

import cn.com.safeinfo.watchdog.common.util.FileUtil;
import cn.com.safeinfo.watchdog.dao.ModuleDao;
import cn.com.safeinfo.watchdog.model.entity.ModuleModel;
import cn.com.safeinfo.watchdog.model.param.UpLoadJarParam;
import cn.com.safeinfo.watchdog.common.util.SystemExecUtil;
import cn.com.safeinfo.watchdog.common.util.SystemInfoUtil;
import cn.com.safeinfo.watchdog.common.result.JsonResult;
import cn.com.safeinfo.watchdog.common.result.ResultUtil;
import cn.com.safeinfo.watchdog.common.result.Status;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import oshi.util.Util;

import java.io.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * 服务模块 服务类
 *
 * @author: ly
 * @date: 2020/3/5 16:38
 */
@Service
public class ModuleService extends ServiceImpl<ModuleDao, ModuleModel> {
    private static final Logger logger = LoggerFactory.getLogger(ModuleService.class);
    @Value("${server.port}")
    private String port;//看门狗的端口

    /**
     * 查询单个服务模块的信息，并更新运行状态
     *
     * @param moduleName 服务名
     * @return
     */
    public JsonResult queryModule(String moduleName) {
        ModuleModel model = this.getById(moduleName);//根据服务名查询服务
        String pidPath = FileUtil.watchPath + File.separator + moduleName + ".pid";//进程号文件
        File pidFile = new File(pidPath);
        if (null == model) {
            return ResultUtil.error(500, Status.SERVICE_NOT_FOUND.value());
        } else if ("0".equals(model.getStatus()) || !pidFile.exists()) {//程序已停止或没有进程号文件
            return ResultUtil.error(500, Status.SERVICE_STOPPED.value());//服务已停止
        }
        String pid = FileUtil.readFile(pidFile);
        String osName = SystemInfoUtil.getOsName();
        Process process = null;//执行命令后的进程
        if (osName.contains("Windows")) {
            process = SystemExecUtil.execCommand("cmd /c jps | findstr \"" + pid + "\"");//查询进程是否存在
        } else if (osName.contains("Linux")) {
            String[] cmd = new String[3];
            cmd[0] = "/bin/sh";
            cmd[1] = "-c";
            cmd[2] = "jps -l | grep " + pid;
            process = SystemExecUtil.execCommand(cmd);//查询进程是否存在
        }
        List<String> cmdResult = SystemExecUtil.getCommandResult(process);//启动jar包的进程号和名称
        if (null == cmdResult || cmdResult.size() != 1) {
            model.setStatus("0");//运行状态，0：停止；1：运行
            Timestamp now = new Timestamp(System.currentTimeMillis());//当前时间
            model.setUpdateTime(now);//更新时间
            this.updateById(model);
            FileUtil.deleteFile(pidPath);//删除进程号文件
            logger.info("{}模块更新状态，服务已停止", moduleName);
            return ResultUtil.error(500, Status.SERVICE_STOPPED.value());
        }
        return ResultUtil.success(model);
    }

    /**
     * 保存服务信息
     *
     * @param file
     * @param param
     * @return
     */
    public JsonResult saveModule(MultipartFile file, UpLoadJarParam param) {
        ModuleModel model = this.getById(param.getServiceName());//根据服务名查询服务
        if (param.getVersion().equals(model.getVersion())) {//如果上传版本等于已有版本
            //先删除jar包，再上传新的jar包
            FileUtil.deleteFile(model.getFilePath());//删除jar包
            logger.info("{}模块上传jar包成功，替换jar包", param.getServiceName());
        } else if (!StringUtils.isEmpty(model.getVersion())) {//如果版本号不相等且不为空
            //更改jar包名，再上传新的jar包
            if (!StringUtils.isEmpty(model.getPreviousFilePath())) {
                FileUtil.deleteFile(model.getPreviousFilePath());//删除上上版本jar包
            }
            File previousFile = FileUtil.renameFile(model.getFilePath());
            model.setPreviousVersion(model.getVersion());//保存上一版本号
            model.setPreviousFilePath(previousFile.getAbsolutePath());//保存上一版本路径
            logger.info("{}模块上传jar包成功，更新版本{}", param.getServiceName(), param.getVersion());
        } else if (null == model) {//如果服务模块不存在
            logger.warn("{}模块上传jar包失败，服务不存在");
            return ResultUtil.error(500, Status.SERVICE_NOT_FOUND.value());
        } else {
            logger.info("{}模块上传jar包成功", param.getServiceName());
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());//当前时间
        File dest = FileUtil.upLoadFile(file);//上传jar包
        BeanUtils.copyProperties(param, model);
        model.setFilePath(dest.getAbsolutePath());//当前版本路径
        model.setServerName(SystemInfoUtil.getHostName());//服务器名称
        model.setWatchdogHost(SystemInfoUtil.getIp());//看门狗ip
        model.setWatchdogPort(port);//看门狗端口
        model.setUpdateTime(now);//更新时间
        this.updateById(model);
        return ResultUtil.success(Status.JAR_UPLOAD_SUCCESS.value());
    }

    /**
     * 启动jar包
     * 远程调试： java -Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=y -jar watchdog-1.0.0.jar
     *
     * @param moduleName
     */
    public JsonResult startModule(String moduleName) {
        ModuleModel model = this.getById(moduleName);//根据服务名查询服务
        if (null == model) {//服务不存在
            return ResultUtil.error(500, Status.SERVICE_NOT_FOUND.value());
        }
        String pidPath = FileUtil.watchPath + File.separator + moduleName + ".pid";//进程号文件
        File pidFile = new File(pidPath);
        if (pidFile.exists()) {
            logger.info("{}模块已启动，无需再次启动", moduleName);
            return ResultUtil.error(500, Status.SERVICE_STARTED.value(), FileUtil.readFile(pidFile));//服务已启动
        }
        String osName = SystemInfoUtil.getOsName();//系统名称
        Process process = null;//启动jar包后的进程
        File jarFile = new File(model.getFilePath());
        if (!jarFile.exists()) {
            logger.warn("{}模块启动失败,无jar包", moduleName);
            return ResultUtil.error(500, Status.JAR_NOT_FOUND.value());//如果jar包不存在
        }
        if (osName.contains("Windows")) {
            process = SystemExecUtil.execCommand("java -jar " + model.getFilePath());//启动jar包
        } else if (osName.contains("Linux")) {
            String[] cmd = new String[3];
            cmd[0] = "/bin/sh";
            cmd[1] = "-c";
            cmd[2] = "java -jar " + model.getFilePath();
            process = SystemExecUtil.execCommand(cmd);//启动jar包
        }
        Long pid = SystemExecUtil.getProcessID(process);//获取进程号
        FileUtil.writeFile(pidFile, pid.toString());//将进程号写入进程文件
        if (pid.equals(-1)) {//如果没有进程号
            logger.warn("{}模块启动失败,无进程号", moduleName);
            return ResultUtil.error(500, Status.SERVICE_STARTED_FAIL.value());
        } else {
            Timestamp now = new Timestamp(System.currentTimeMillis());//当前时间
            model.setStatus("1");//设置服务状态已启动
            model.setUpdateTime(now);
            this.updateById(model);
            logger.info("{}模块启动成功", moduleName);
            return ResultUtil.success(Status.SERVICE_STARTED_SUCCESS.value());
        }
    }

    /**
     * 停止jar包
     *
     * @param moduleName
     */
    public JsonResult stopModule(String moduleName) {
        ModuleModel model = this.getById(moduleName);//根据服务名查询服务
        if (null == model) {
            return ResultUtil.error(500, Status.SERVICE_NOT_FOUND.value());
        }
        String pidPath = FileUtil.watchPath + File.separator + moduleName + ".pid";
        File pidFile = new File(pidPath);
        if (!pidFile.exists()) {
            logger.warn("{}模块停止失败,无进程号", moduleName);
            return ResultUtil.error(500, Status.SERVICE_STOPPED.value());//进程号文件不存在
        }
        String pid = FileUtil.readFile(pidFile);//进程号
        String osName = SystemInfoUtil.getOsName();
        if (osName.contains("Windows")) {
            SystemExecUtil.execCommand("taskkill /pid " + pid + " -t -f");//杀进程
        } else if (osName.contains("Linux")) {
            String[] cmd = new String[3];
            cmd[0] = "/bin/sh";
            cmd[1] = "-c";
            cmd[2] = "kill -9 " + pid;
            SystemExecUtil.execCommand(cmd);//杀进程
        }
        FileUtil.deleteFile(pidPath);//删除存进程号文件
        Timestamp now = new Timestamp(System.currentTimeMillis());//当前时间
        model.setStatus("0");//设置服务状态已启动
        model.setUpdateTime(now);
        this.updateById(model);
        logger.info("{}模块停止成功", moduleName);
        return ResultUtil.success(Status.SERVICE_STOPPED_SUCCESS.value());
    }

    /**
     * 重启jar包
     *
     * @param moduleName
     * @return
     */
    public JsonResult restartModule(String moduleName) {
        logger.info("{}模块正在重启···", moduleName);
        stopModule(moduleName);//停止jar包
        Util.sleep(1000);
        return startModule(moduleName);//启动jar包
    }

}
