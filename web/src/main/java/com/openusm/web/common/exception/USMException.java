/**
 * Copyright (C), 2019-2020
 */
package com.openusm.web.common.exception;

import com.openusm.web.common.vo.ResultCodeEnum;
import lombok.Data;

/**
 *
 * 2020/3/21 22:08
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 *
 */
@Data
public class USMException extends RuntimeException {
    private Integer code;

    public USMException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public USMException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "USMException{" + "code=" + code + ", message=" + this.getMessage() + '}';
    }
}
