package com.openusm.web.annotation;

import com.openusm.web.common.vo.Module;
import com.openusm.web.common.vo.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/22 18:31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
    Module mod();
    Operation opt();
    String msg() default "";
}
