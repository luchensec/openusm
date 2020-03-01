package com.openusm.web.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/22 17:55
 */
@Data
@TableName("sys_user")
public class User {

    @TableId(type=IdType.AUTO)
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String salt;
    private String email;
    private String mobile;
    private Integer status;
    private Long deptId;
    @TableField(exist=false)
    private String deptName;
    private Date ctime;
    private Date mtime;
    private Date lastLoginTime;
}
