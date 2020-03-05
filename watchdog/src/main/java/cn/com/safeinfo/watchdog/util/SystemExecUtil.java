package cn.com.safeinfo.watchdog.util;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.jna.platform.windows.Kernel32;

import java.lang.reflect.Field;

/**
 * 系统内操作工具类
 *
 * @author: ly
 * @date: 2020/3/5 13:58
 */
public class SystemExecUtil {
    private static final Logger logger = LoggerFactory.getLogger(SystemExecUtil.class);

    /**
     * 获取进程号
     *
     * @param p 进程
     * @return
     */
    public static long getProcessID(Process p) {
        long pid = -1;
        try {
            //for windows
            if (p.getClass().getName().equals("java.lang.Win32Process") ||
                    p.getClass().getName().equals("java.lang.ProcessImpl")) {
                Field f = p.getClass().getDeclaredField("handle");
                f.setAccessible(true);
                long handl = f.getLong(p);
                Kernel32 kernel = Kernel32.INSTANCE;
                WinNT.HANDLE hand = new WinNT.HANDLE();
                hand.setPointer(Pointer.createConstant(handl));
                pid = kernel.GetProcessId(hand);
                f.setAccessible(false);
            }
            //for unix based operating systems
            else if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p);
                f.setAccessible(false);
            }
        } catch (Exception ex) {
            pid = -1;
        }
        logger.info("进程号：{}", pid);
        return pid;
    }

    public static Process execCommand(String cmd) throws Exception {
        Process process = Runtime.getRuntime().exec(cmd);
        //int status = process.waitFor();
        logger.info("执行命令：{}", cmd);
        return process;
    }
    
}
