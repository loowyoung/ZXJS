package cn.com.safeinfo.watchdog.common.result;

public class ResultUtil {

    public static JsonResult success(Object data) {
        return new JsonResult(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }

    public static JsonResult success(int count) {
        return new JsonResult(ResultEnum.SUCCESS.getCode(), "执行成功了" + count + "条记录");
    }

    public static JsonResult error(Integer code) {
        return new JsonResult(code, null);
    }

    public static JsonResult error(Integer code, String msg) {
        return new JsonResult(code, msg);
    }

    public static JsonResult error(Integer code, String msg, Object errorData) {
        return new JsonResult(code, msg, errorData);
    }

}
