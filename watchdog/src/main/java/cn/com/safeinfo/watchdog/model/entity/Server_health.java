package cn.com.safeinfo.watchdog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器运行状态监控
 *
 * @author: ly
 * @date: 2020/3/4 13:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Server_health {
    private String serverName;//服务器名称
    private String monitoredAt;//监测时间，Linux毫秒时间戳
    private String cpu;//CPU使用率，从0到1
    private String memory;//内存使用率，从0到1
    private String disk;//监测时间，Linux毫秒时间戳
    private String connection;//服务器连接数
    private String createdAt;//创建时间，Linux毫秒时间戳
}
