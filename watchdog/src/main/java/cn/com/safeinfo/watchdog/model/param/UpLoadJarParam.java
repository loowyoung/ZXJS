package cn.com.safeinfo.watchdog.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上传服务模块的jar包
 *
 * @author: ly
 * @date: 2020/3/6 7:51
 */
@Data
@ApiModel("服务模块")
public class UpLoadJarParam {
    @ApiModelProperty(value = "服务模块", required = true)
    private String serviceName;
    @ApiModelProperty(value = "服务版本", required = true)
    private String version;
    @ApiModelProperty(value = "服务描述", required = true)
    private String serviceDesc;
}
