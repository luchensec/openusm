/**
 * Copyright (C), 2019-2020
 */
package com.openusm.collector.event;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 *
 * 2020/3/9 20:38
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 *
 */
//@Slf4j
@Component
public class SnmpTrapHandler implements CommandResponder  {
    @Autowired
    private EventConfig eventConfig;

    private static String ipAddress = "udp:127.0.0.1/162";
    private Snmp snmp = null;
    public void init() {
        //1、初始化多线程消息转发类
        ThreadPool threadPool = ThreadPool.create("SnmpTrap", eventConfig.getSnmptrapthreadnum());
        MessageDispatcher messageDispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
        //其中要增加三种处理模型。如果snmp初始化使用的是Snmp(TransportMapping<? extends Address> transportMapping) ,就不需要增加
        messageDispatcher.addMessageProcessingModel(new MPv1());
        messageDispatcher.addMessageProcessingModel(new MPv2c());
        OctetString localEngineID = new OctetString(MPv3.createLocalEngineID());
        USM usm = new USM(SecurityProtocols.getInstance().addDefaultProtocols(), localEngineID, 0);

        // 不同的协议就是OID不同
        UsmUser user = new UsmUser(new OctetString(eventConfig.getSnmpusername()), AuthSHA.ID,
                new OctetString(eventConfig.getSnmpuserauthenticationpassword()),
                PrivAES128.ID, new OctetString(eventConfig.getSnmpuserprivpassword()));
        usm.addUser(user.getSecurityName(), user);
        messageDispatcher.addMessageProcessingModel(new MPv3(usm));
        //2、创建transportMapping
        TransportMapping<?> transportMapping = null;
        try {
            UdpAddress udpAddr = (UdpAddress) GenericAddress.parse(ipAddress);
            transportMapping = new DefaultUdpTransportMapping(udpAddr);
            //3、正式创建snmp
            snmp = new Snmp(messageDispatcher, transportMapping);
            //开启监听
            snmp.listen();
        } catch (IOException e) {
//            log.error("初始化transportMapping失败：", e.getMessage());
        }
    }

    public void start() {
        init();
        snmp.addCommandResponder(this);
//        log.info("SNMP Trap Listenering port");
    }

    @Override
    public void processPdu(CommandResponderEvent event) {
        String version = null;
        String community = null;
        if (event.getPDU().getType() == PDU.V1TRAP) {
            version = "v1";
            community = new String(event.getSecurityName());
        } else if (event.getPDU().getType() == PDU.TRAP) {
            if (event.getSecurityModel() == 2) {
                version = "v2";
                community = new String(event.getSecurityName());
            } else {
                version = "v3";
            }
        }
        Vector<? extends VariableBinding> variableBindings = event.getPDU().getVariableBindings();

        for (VariableBinding binding : variableBindings) {
            System.out.println(binding.getOid()+":" + binding.getVariable());
        }

        System.out.println("接收到的trap信息：[发送来源="+event.getPeerAddress()+",snmp版本="+version+",团体名="+community+", 携带的变量="+event.getPDU().getVariableBindings()+"]");
    }

}
