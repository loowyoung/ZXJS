package cn.com.safeinfo.watchdog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import static java.lang.System.getProperty;

/**
 * 系统信息获取工具类
 *
 * @author: ly
 * @date: 2020/3/4 14:24
 */
public class SystemInfoUtil {
    private static final Logger logger = LoggerFactory.getLogger(SystemInfoUtil.class);
    private static SystemInfo systemInfo = new SystemInfo();
    private static HardwareAbstractionLayer hal = systemInfo.getHardware();
    private static GlobalMemory memory = hal.getMemory();
    private static OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

    /**
     * 获取服务器CPU使用率
     *
     * @return
     */
    public static float getServerCpuUsage() {
        CentralProcessor processor = hal.getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        //等1秒
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        //在用户级(应用程序)执行时发生的CPU利用率。
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        //在用户级以nice优先级执行时发生的CPU利用率。
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        //在系统级(内核)执行时发生的CPU利用率。
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        //CPU或CPU处于空闲状态且系统没有未完成的磁盘I/O请求的时间。
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        //当系统有一个未完成的磁盘I/O请求时，CPU或CPU处于空闲状态的时间。
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        //CPU用于服务硬件irq的时间
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        //CPU用于服务软irq的时间
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        //系统管理程序为系统中的其他来宾分配的时间。只支持Linux。
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        float cpuUsage = Float.valueOf(user + sys) / Float.valueOf(totalCpu);
        logger.debug("CPU使用率：{}。CPU使用量：user-{},sys-{}，totalCpu-{}", cpuUsage, user, sys, totalCpu);
        return cpuUsage;
    }

    /**
     * 获取单个服务的CPU使用率
     *
     * @param pid 进程号
     * @return
     */
    public static float getServiceCpuUsage(int pid) {
        //通过进程号获取进程
        OSProcess p = operatingSystem.getProcess(pid);
        float cpuUsage = (float) p.calculateCpuPercent();
        logger.debug("进程号：{},CPU使用率：{}", pid, cpuUsage);
        return cpuUsage;
    }

    /**
     * 获取服务器内存使用率
     *
     * @return
     */
    public static float getServerMemoryUsage() {
        long available = memory.getAvailable();
        long total = memory.getTotal();
        float memoryUsage = Float.valueOf(total - available) / Float.valueOf(total);
        //使用量单位是Byte
        logger.info("memory使用率：{}。memory使用量：available-{},total-{}", memoryUsage, available, total);
        return memoryUsage;
    }

    /**
     * 获取单个服务的内存使用率
     *
     * @param pid 进程号
     * @return
     */
    public static float getServiceMemoryUsage(int pid) {
        //通过进程号获取进程
        OSProcess p = operatingSystem.getProcess(pid);
        float memoryUsage = Float.valueOf(p.getResidentSetSize()) / Float.valueOf(memory.getTotal());
        logger.info("进程号：{},memory使用率：{},memory使用量：{}", pid, memoryUsage, p.getResidentSetSize());
        return memoryUsage;
    }

    /**
     * 获取服务器磁盘使用率
     *
     * @return
     */
    public static float getServerDiskUsage() {
        HWDiskStore[] diskStores = hal.getDiskStores();
        long total = 0;
        long used = 0;
        if (diskStores != null && diskStores.length > 0) {
            for (HWDiskStore diskStore : diskStores) {
                long size = diskStore.getSize();
                long writeBytes = diskStore.getWriteBytes();
                total += size;
                used += writeBytes;
            }
        }
        float diskUsage = Float.valueOf(used) / Float.valueOf(total);
        logger.info("磁盘使用率：{}。磁盘使用量：used-{},total-{}", diskUsage, used, total);
        return diskUsage;
    }

    /**
     * 获取操作系统名称
     *
     * @return
     */
    public static String getOsName() {
        String osName = getProperty("os.name");
        String archName = getProperty("os.arch");
        logger.info("操作系统名称：{}。架构名称：{}", osName, archName);
        return osName;
    }

}
