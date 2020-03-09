package cn.com.safeinfo.watchdog.common.util;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.jna.platform.windows.Kernel32;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    /**
     * 执行windows命令
     *
     * @param cmd
     * @return
     */
    public static Process execCommand(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            //int status = process.waitFor();
            logger.info("执行命令：{}", cmd);
            return process;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("执行命令失败：{}", cmd);
            return null;
        }
    }

    /**
     * 执行Linux命令你
     *
     * @param cmd
     * @return
     */
    public static Process execCommand(String[] cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            logger.info("执行命令：{}", Arrays.toString(cmd));
            return process;
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("执行命令失败：{}", Arrays.toString(cmd));
            return null;
        }
    }

    /**
     * 获取进程执行的结果
     *
     * @param process
     * @return
     */
    public static List<String> getCommandResult(Process process) {
        List<String> result = new ArrayList<>();
        BufferedReader bufrIn;
        BufferedReader bufrError;
        try {
            // 方法阻塞, 等待命令执行完成（成功会返回0）
            int execResult = process.waitFor();
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
            // 读取输出
            String line;
            while ((line = bufrIn.readLine()) != null) {
                result.add(line);
            }
            while ((line = bufrError.readLine()) != null) {
                result.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("获取进程执行结果失败：{}", e.getMessage());
        }
        return result;
    }

}
