package cn.com.safeinfo.watchdog.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务模块 展示类
 *
 * @author: ly
 * @date: 2020/3/5 17:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("服务模块")
public class ModuleVo {
    @ApiModelProperty("服务名称")
    private String serviceName;//主键，固定的服务名称
    @ApiModelProperty("服务描述")
    private String serviceDesc;
    @ApiModelProperty("服务器名称")
    private String serverName;
    @ApiModelProperty("服务版本")
    private String version;
    @ApiModelProperty("服务jar包路径")
    private String filePath;//服务jar包路径（为了支持服务的开机启动，这里应该是一个固定的路径）
    @ApiModelProperty("上个版本")
    private String previousVersion;//上个版本，用于回滚发布
    @ApiModelProperty("上个版本路径")
    private String previousFilePath;
    @ApiModelProperty("运行状态，0：停止；1：运行")
    private String status;
    @ApiModelProperty("最近更新时间")
    private String updateTime;
    @ApiModelProperty("看门狗服务地址")
    private String watchdogHost;//看门狗服务地址，IP或Hostname
    @ApiModelProperty("看门狗服务端口")
    private String watchdogPort;
}
