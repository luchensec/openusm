package com.openusm.web.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/22 18:12
 */
@Data
@TableName("audit_log")
public class AuditLog {

    @TableId(type= IdType.AUTO)
    private Long id;
    private String username;
    private String operation;
    private String method;
    private String params;
    private String clientIp;
    private String funModule;
    private String message;
    private Integer result;
    private Date ctime;
}
