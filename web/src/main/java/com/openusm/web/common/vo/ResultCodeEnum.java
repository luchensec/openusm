package com.openusm.web.common.vo;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(true,50000,"成功"),
    PARAM_ERROR(false,50001,"参数错误"),
    NULL_POINT(false,50002,"参数错误"),
    UNKNOWN_ERROR(false,50010,"未知错误"),
    ;

    // 响应是否成功
    private Boolean success;
    // 响应状态码
    private Integer code;
    // 响应信息
    private String message;

    ResultCodeEnum(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
