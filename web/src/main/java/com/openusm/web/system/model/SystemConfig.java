package com.openusm.web.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/22 18:07
 */
@Data
@TableName("sys_config")
public class SystemConfig {

    @TableId(type= IdType.AUTO)
    private Long id;
    private String paramName;
    private String paramValue;
    private Integer status;
    private String remark;
}
