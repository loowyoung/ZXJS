package cn.com.safeinfo.watchdog.util.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用json返回结果
 */
public class JsonResult {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回内容
     */
    private Object obj;

    public JsonResult() {
    }

    public JsonResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(Integer code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String toString() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("code", this.getCode());
            map.put("msg", this.getMsg());
            map.put("obj", this.getObj());
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "JsonResult(code=" + this.getCode() + ", msg=" + this.getMsg() + ", obj=" + this.getObj() + ")";
    }

}
