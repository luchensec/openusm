/**
 * Copyright (C), 2019-2020
 */
package com.openusm.web.common.exception;

import com.openusm.web.common.vo.R;
import com.openusm.web.common.vo.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * 2020/3/21 22:10
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 *
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**-------- 通用异常处理方法 --------**/
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e) {
        log.error("USM内部异常", e);
        return R.error();    // 通用异常结果
    }

    /**-------- 指定异常处理方法 --------**/
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public R error(NullPointerException e) {
        log.error("USM空指针异常", e);
        return R.setResult(ResultCodeEnum.NULL_POINT);
    }


    /**-------- 自定义定异常处理方法 --------**/
    @ExceptionHandler(USMException.class)
    @ResponseBody
    public R error(USMException e) {
        log.error("USM业务逻辑异常", e);
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
