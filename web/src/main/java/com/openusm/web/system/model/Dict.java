package com.openusm.web.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/22 18:09
 */
@Data
@TableName("sys_dict")
public class Dict {

    @TableId(type= IdType.AUTO)
    private Long id;
    private String dicName;
    private String dicType;
    private String dicKey;
    private String dicValue;
    private Integer orderNum;
    private String remark;
}
