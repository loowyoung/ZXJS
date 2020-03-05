package cn.com.safeinfo.watchdog.util.result;

public enum ResultEnum {

    UNKNOWN_ERROR(-1, "未知错误"),
    SUCCESS(200, "正确"),
    PARAM_ERROR(400, "参数错误"),
    PERM_ERROR(401, "权限错误"),
    NO_LOGIN(403, "用户未登录"),
    METHOD_ERROR(405, "方法不允许"),
    SYSTEM_ERROR(500, "系统错误"),
    TIMEOUT_ERROR(504, "请求超时");

    private int code;

    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
