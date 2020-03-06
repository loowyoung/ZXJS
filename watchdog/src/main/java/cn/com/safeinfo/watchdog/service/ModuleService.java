package cn.com.safeinfo.watchdog.service;

import cn.com.safeinfo.watchdog.dao.ModuleDao;
import cn.com.safeinfo.watchdog.model.entity.ModuleModel;
import cn.com.safeinfo.watchdog.model.param.UpLoadJarParam;
import cn.com.safeinfo.watchdog.util.SystemExecUtil;
import cn.com.safeinfo.watchdog.util.SystemInfoUtil;
import cn.com.safeinfo.watchdog.util.result.JsonResult;
import cn.com.safeinfo.watchdog.util.result.ResultUtil;
import cn.com.safeinfo.watchdog.util.result.Status;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;

/**
 * 服务模块 服务类
 *
 * @author: ly
 * @date: 2020/3/5 16:38
 */
@Service
public class ModuleService extends ServiceImpl<ModuleDao, ModuleModel> {
    private static final Logger logger = LoggerFactory.getLogger(ModuleService.class);
    //watchdog的jar包所在路径
    private static final String watchPath = System.getProperty("user.dir") + File.separator + "watchdog-projects";

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
            deleteFile(model.getFilePath());//删除jar包
        } else if (!StringUtils.isEmpty(model.getVersion())) {//如果版本号不相等且不为空
            //更改jar包名，再上传新的jar包
            if (!StringUtils.isEmpty(model.getPreviousFilePath())) {
                deleteFile(model.getPreviousFilePath());//删除上上版本jar包
            }
            File previousFile = renameFile(model.getFilePath());
            model.setPreviousVersion(model.getVersion());//保存上一版本号
            model.setPreviousFilePath(previousFile.getAbsolutePath());//保存上一版本路径
        } else if (null == model) {//如果服务模块不存在
            return ResultUtil.error(500, Status.SERVICE_NOT_FOUND.value());
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());//当前时间
        File dest = upLoadFile(file);//上传jar包
        BeanUtils.copyProperties(param, model);
        model.setFilePath(dest.getAbsolutePath());//当前版本路径
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
        String osName = SystemInfoUtil.getOsName();//系统名称
        Process process = null;//启动jar包
        File jarFile = new File(model.getFilePath());
        if (!jarFile.exists()) {
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
        File pidFile = new File(watchPath + File.separator + moduleName + ".pid");//创建进程文件
        deleteFile(pidFile.getAbsolutePath());//删除存在的进程文件
        writeFile(pidFile, pid.toString());//将进程号写入进程文件
        if (pid.equals(-1)) {//如果没有进程号
            return ResultUtil.error(500, Status.SERVICE_STARTED_FAIL.value());
        } else {
            Timestamp now = new Timestamp(System.currentTimeMillis());//当前时间
            model.setStatus("1");//设置服务状态已启动
            model.setUpdateTime(now);
            this.updateById(model);
            return ResultUtil.success(Status.SERVICE_STARTED.value());
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
        String pidPath = watchPath + File.separator + moduleName + ".pid";
        File pidFile = new File(pidPath);
        if (!pidFile.exists()) {
            return ResultUtil.error(500, Status.SERVICE_STOPPED.value());//进程号文件不存在
        }
        String pid = readFile(pidFile);//进程号
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
        deleteFile(pidPath);//删除存进程号文件
        Timestamp now = new Timestamp(System.currentTimeMillis());//当前时间
        model.setStatus("0");//设置服务状态已启动
        model.setUpdateTime(now);
        this.updateById(model);
        return ResultUtil.success(Status.SERVICE_STOPPED.value());
    }

    /**
     * 删除jar包
     *
     * @param jarPath
     * @return
     */
    private boolean deleteFile(String jarPath) {
        File file = new File(jarPath);
        if (!file.exists()) {//如果文件不存在
            return false;
        }
        file.delete();
        logger.info("文件删除：{}", file.getAbsolutePath());
        return true;
    }

    /**
     * 重命名jar包
     *
     * @param jarPath
     * @return
     */
    private File renameFile(String jarPath) {
        File file = new File(jarPath);
        if (!file.exists()) {//如果文件不存在
            return null;
        }
        File newfile = new File(jarPath + ".bak");
        file.renameTo(newfile);
        logger.info("文件重命名：{}\n{}", file.getAbsolutePath(), newfile.getAbsolutePath());
        return newfile;//返回更改后的文件
    }

    /**
     * 上传jar包到服务器
     *
     * @param file jar包
     * @return
     */
    private File upLoadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        File dest = new File(watchPath + File.separator + fileName);
        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            logger.info("文件上传：{}", dest);
            return dest;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 文件写入
     *
     * @param file
     * @param content
     */
    private void writeFile(File file, String content) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile()); //表示不追加
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            logger.info("写入文件位置：{}，写入内容：{}", file.getAbsolutePath(), content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件内容读取
     *
     * @param file
     * @return
     */
    private String readFile(File file) {
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;
        StringBuilder result = new StringBuilder();//文件内容
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String data;
            while ((data = br.readLine()) != null) {
                result.append(data);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("读取文件位置：{}，文件内容：{}", file.getAbsolutePath(), result);
        return result.toString();
    }

}
