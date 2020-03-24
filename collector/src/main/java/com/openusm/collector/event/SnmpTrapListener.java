/**
 * Copyright (C), 2019-2020
 */
package com.openusm.collector.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 2020/3/9 19:44
 *
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 */
@Component
public class SnmpTrapListener implements CommandLineRunner {

    @Autowired
    private SnmpTrapHandler snmpTrapHandler;

    @Override
    public void run(String... args) throws Exception {
        snmpTrapHandler.start();
    }
}
