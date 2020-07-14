package com.liuyong.licensetesting.utils;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.NetworkParams;
import oshi.software.os.OperatingSystem;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 系统信息
 *
 * @author ly
 * @date 2020年 07月13日 17:48:46
 */
@Slf4j
public class SystemInfoUtil {
    private static final SystemInfo systemInfo = new SystemInfo();//系统信息
    private static final HardwareAbstractionLayer hal = systemInfo.getHardware();//硬件抽象层。提供对硬件项（如处理器、内存、电池和磁盘）的访问。
    private static final OperatingSystem os = systemInfo.getOperatingSystem();//操作系统
    private static final ComputerSystem cs = hal.getComputerSystem();//代表计算机系统/产品的物理硬件，包括BIOS /固件和主板，逻辑板等。
    private static final CentralProcessor cp = hal.getProcessor();//代表计算机系统的整个中央处理单元（CPU）
    private static final NetworkParams np = os.getNetworkParams();//提供正在运行的操作系统的网络参数

    /**
     * 用户账户名称
     *
     * @return
     */
    public static String getUserName() {
        return np.getHostName() + "-" + System.getProperty("user.name");
    }

    /**
     * 操作系统的名称
     *
     * @return
     */
    public static String getOsName() {
        return System.getProperty("os.name");
    }

    /**
     * jar包所在路径
     *
     * @return
     */
    public static String getJarPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取主机IP
     *
     * @return
     */
    public static String getIp() {
        String ip = getLocalhost().getHostAddress();//获取计算机IP
        return ip;
    }

    /**
     * 获取主板序列号
     *
     * @return
     */
    public static String getMainBoardNum() {
        return cs.getBaseboard().getSerialNumber();
    }

    /**
     * 获取CPU序列号
     *
     * @return
     */
    public static String getCpuId() {
        return cp.getProcessorIdentifier().getProcessorID();
    }

    /**
     * 获取本地主机
     *
     * @return
     */
    private static InetAddress getLocalhost() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ia;
    }

    public static void main(String[] args) {
        System.out.println(getUserName());
        System.out.println(getOsName());
        System.out.println(getMainBoardNum());
        System.out.println(getCpuId());
        System.out.println(getIp());
    }
}
