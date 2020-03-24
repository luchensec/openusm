/**
 * Copyright (C), 2019-2020
 */
package com.openusm.web.common.exception;

import com.openusm.web.common.vo.ResultCodeEnum;

/**
 *
 * 2020/3/21 22:33
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 *
 */
public class ExceptionUtils {

    public static void throwException(ResultCodeEnum resultCode) {
        throw new USMException(resultCode);
    }

}
