/**
 * Copyright (C), 2019-2020
 */
package com.openusm.web.common.vo;

import lombok.Getter;
import org.springframework.ui.Model;

/**
 *
 * 2020/3/22 19:59
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 *
 */
@Getter
public enum Module {
    ASSET(1, "资产管理"),
    EVENT(2, "事件管理"),
    ALARM(3, "告警管理"),
    SYSTEM(4, "系统管理")
    ;
    private Integer code;
    private String name;

    Module(Integer code, String name) {
        this.name = name;
    }
}
