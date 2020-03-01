package com.openusm.web.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/26 2:05
 */
@Data
public class RestResponse {

    /** 返回页面的数据 */
    private Object data;
    /** 返回页面的消息 */
    private String msg;
    /** 标识业务处理成功或失败0:失败 */
    private int stateCode;

    public RestResponse() {
        super();
    }

    public RestResponse(Object data, String msg, int stateCode) {
        this.data = data;
        this.msg = msg;
        this.stateCode = stateCode;
    }

    public static RestResponse success(Object data) {
        return success(data, 1);
    }

    public static RestResponse success(String msg) {
        return success(msg, 1);
    }

    public static RestResponse success(int stateCode) {
        return success(null, stateCode);
    }

    public static RestResponse success(String msg, int stateCode) {
        return success(null, msg,  stateCode);
    }

    public static RestResponse success(Object data, int stateCode) {
        return success(data, null, stateCode);
    }

    public static RestResponse success(Object data, String msg) {
        return success(data, msg, 1);
    }

    public static RestResponse success(Object data, String msg, int stateCode) {
        return new RestResponse(data, msg, stateCode);
    }

    public static RestResponse error(Object data, String msg, int stateCode) {
        return new RestResponse(data, msg, stateCode);
    }

    public static RestResponse error(Object data, String msg) {
        return error(data, msg, 0);
    }

    public static RestResponse error(Object data, int stateCode) {
        return error(data, null, stateCode);
    }

    public static RestResponse error(String msg, int stateCode) {
        return error(null, msg, stateCode);
    }

    public static RestResponse error(Object data) {
        return error(data, null);
    }

    public static RestResponse error(String msg) {
        return error(null, msg);
    }

    public static RestResponse error(int stateCode) {
        return error(null, stateCode);
    }
}
