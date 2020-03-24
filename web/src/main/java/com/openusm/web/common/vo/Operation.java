/**
 * Copyright (C), 2019-2020
 */
package com.openusm.web.common.vo;

import lombok.Getter;

/**
 *
 * 2020/3/22 22:28
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 *
 */
@Getter
public enum Operation {
    LOGIN(1, "登录"),
    LOGOUT(2, "登出"),
    EXPORT(3, "导出"),
    IMPORT(4, "导入"),
    INSERT(5, "新增"),
    DELETE(6, "删除"),
    UPDATE(7, "更新"),
    SEARCH(8, "查询")
    ;

    private Integer code;
    private String name;

    Operation(Integer code, String name) {
        this.name = name;
    }
}
