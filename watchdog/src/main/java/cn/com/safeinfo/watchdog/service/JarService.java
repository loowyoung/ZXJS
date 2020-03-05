package cn.com.safeinfo.watchdog.service;

import cn.com.safeinfo.watchdog.util.SystemExecUtil;
import cn.com.safeinfo.watchdog.util.SystemInfoUtil;
import org.springframework.stereotype.Service;

/**
 * @author: ly
 * @date: 2020/3/5 13:41
 */
@Service
public class JarService {
    public void runJar() {
        try {
            System.out.println(SystemInfoUtil.getOsName());
            String commend = "java -jar F:\\IdeaProjects\\hrmw-emergency\\target\\hrmw-emergency-1.0.0.jar";
            Process p = SystemExecUtil.execCommand(commend);
            SystemExecUtil.getProcessID(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopJar(String pid) {
        //taskkill /pid 9396 -t
        try {
            String commend = "taskkill /pid " + pid + " -t -f";
            Process p = SystemExecUtil.execCommand(commend);
            //SystemExecUtil.getProcessID(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
