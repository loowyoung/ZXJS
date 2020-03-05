package cn.com.safeinfo.watchdog.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模块实体类
 * hrmw-collector-symlink
 * hrmw-collector-hdzx（山东专用服务）
 * hrmw-collect-service
 * hrmw-data-consumer
 * hrmw-data-transporter
 * hrmw-data-archiver
 * hrmw-cache-manager
 *
 * @author: ly
 * @date: 2020/3/5 12:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys.tb_sys_service_module")
public class ModuleModel {
    @TableField("service_name")
    private String serviceName;//主键，固定的服务名称
    @TableField("service_desc")
    private String serviceDesc;//服务描述
    @TableField("server_name")
    private String serverName;//服务器名称
    @TableField("version")
    private String version;//服务版本
    @TableField("file_path")
    private String filePath;//服务jar包路径（为了支持服务的开机启动，这里应该是一个固定的路径）
    @TableField("previous_version")
    private String previousVersion;//上个版本，用于回滚发布
    @TableField("previous_file_path")
    private String previousFilePath;//上个版本路径
    @TableField("status")
    private String status;//运行状态，0：停止；1：运行
    @TableField("update_time")
    private String updateTime;//最近更新时间
    @TableField("watchdog_host")
    private String watchdogHost;//看门狗服务地址，IP或Hostname
    @TableField("watchdog_port")
    private String watchdogPort;//看门狗服务端口

}
