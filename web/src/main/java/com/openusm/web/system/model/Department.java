package com.openusm.web.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/22 14:48
 */
@Data
@TableName("sys_dept")
public class Department {

    @TableId(type= IdType.AUTO)
    private Long id;
    private Long parentId;
    private String deptName;
    private Integer orderNum;
}
