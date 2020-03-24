/**
 * Copyright (C), 2019-2020
 */
package com.openusm.collector.event;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * 2020/3/9 19:56
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 *
 */
@Configuration
@PropertySource("classpath:event.properties")
@ConfigurationProperties(prefix = "event")
public class EventConfig {
    private Integer syslogudpport;
    private Integer syslogtcpport;
    private Integer snmptrapport;
    private Integer snmptrapthreadnum;
    private String snmptrapencode;
    private Boolean snmpuseauthentication;
    private String snmpusername;
    private String snmpuserauthenticationpassword;
    private String snmpauthenticationprotocol;
    private Boolean snmpuseprivacy;
    private String snmpuserprivpassword;

    public Integer getSyslogudpport() {
        return syslogudpport;
    }

    public void setSyslogudpport(Integer syslogudpport) {
        this.syslogudpport = syslogudpport;
    }

    public Integer getSyslogtcpport() {
        return syslogtcpport;
    }

    public void setSyslogtcpport(Integer syslogtcpport) {
        this.syslogtcpport = syslogtcpport;
    }

    public Integer getSnmptrapport() {
        return snmptrapport;
    }

    public void setSnmptrapport(Integer snmptrapport) {
        this.snmptrapport = snmptrapport;
    }

    public Integer getSnmptrapthreadnum() {
        return snmptrapthreadnum;
    }

    public void setSnmptrapthreadnum(Integer snmptrapthreadnum) {
        this.snmptrapthreadnum = snmptrapthreadnum;
    }

    public String getSnmptrapencode() {
        return snmptrapencode;
    }

    public void setSnmptrapencode(String snmptrapencode) {
        this.snmptrapencode = snmptrapencode;
    }

    public Boolean getSnmpuseauthentication() {
        return snmpuseauthentication;
    }

    public void setSnmpuseauthentication(Boolean snmpuseauthentication) {
        this.snmpuseauthentication = snmpuseauthentication;
    }

    public String getSnmpusername() {
        return snmpusername;
    }

    public void setSnmpusername(String snmpusername) {
        this.snmpusername = snmpusername;
    }

    public String getSnmpuserauthenticationpassword() {
        return snmpuserauthenticationpassword;
    }

    public void setSnmpuserauthenticationpassword(String snmpuserauthenticationpassword) {
        this.snmpuserauthenticationpassword = snmpuserauthenticationpassword;
    }

    public String getSnmpauthenticationprotocol() {
        return snmpauthenticationprotocol;
    }

    public void setSnmpauthenticationprotocol(String snmpauthenticationprotocol) {
        this.snmpauthenticationprotocol = snmpauthenticationprotocol;
    }

    public Boolean getSnmpuseprivacy() {
        return snmpuseprivacy;
    }

    public void setSnmpuseprivacy(Boolean snmpuseprivacy) {
        this.snmpuseprivacy = snmpuseprivacy;
    }

    public String getSnmpuserprivpassword() {
        return snmpuserprivpassword;
    }

    public void setSnmpuserprivpassword(String snmpuserprivpassword) {
        this.snmpuserprivpassword = snmpuserprivpassword;
    }
}
