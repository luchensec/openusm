package com.openusm.web.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/22 14:41
 */
@Data
@TableName("sys_menu")
public class Menu {

    @TableId(type= IdType.AUTO)
    private Long id;
    private Long parentId;
    private String menuName;
    private String url;
    private String perms;
    private Integer permType;
    private String icon;
    private Integer orderNum;
}
