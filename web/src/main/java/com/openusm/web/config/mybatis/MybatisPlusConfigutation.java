/**
 * Copyright (C), 2019-2020
 */
package com.openusm.web.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * 2020/2/29 20:01
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 *
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfigutation {

    /**
     * 分页插件
     * @return com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
