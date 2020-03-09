package cn.com.safeinfo.watchdog.common.result;

import lombok.AllArgsConstructor;

/**
 * @author: ly
 * @date: 2020/3/6 15:34
 */
@AllArgsConstructor
public enum Status {
    SERVICE_NOT_FOUND("服务模块不存在"),
    SERVICE_STARTED("服务已启动"),
    SERVICE_STARTED_FAIL("服务启动失败"),
    SERVICE_STOPPED("服务已停止"),
    JAR_UPLOAD_SUCCESS("服务jar包上传成功"),
    JAR_NOT_FOUND("服务jar包不存在"),
    JAR_UPLOAD_FAIL("服务jar包上传失败");

    private String value = null;

    public String value() {
        return this.value;
    }
}
