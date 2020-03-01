package com.openusm.web.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/22 17:59
 */
@Data
@TableName("sys_role")
public class Role {

    @TableId(type= IdType.AUTO)
    private Long id;
    private String roleName;
    private String remark;
    private Date ctime;
    private Date mtime;

}
