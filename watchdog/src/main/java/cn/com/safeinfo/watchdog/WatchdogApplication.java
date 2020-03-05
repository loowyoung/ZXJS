package cn.com.safeinfo.watchdog;

import cn.com.safeinfo.watchdog.netty.NettyServer;
import cn.com.safeinfo.watchdog.util.SystemInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WatchdogApplication implements CommandLineRunner {

    @Autowired
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(WatchdogApplication.class, args);
    }

    @Override
    public void run(String... args) {
        nettyServer.bind(8888);
        SystemInfoUtil.getServerCpuUsage();
        SystemInfoUtil.getServiceCpuUsage(61176);
        SystemInfoUtil.getServerMemoryUsage();
        SystemInfoUtil.getServiceMemoryUsage(61176);
        SystemInfoUtil.getServerDiskUsage();
    }

}
