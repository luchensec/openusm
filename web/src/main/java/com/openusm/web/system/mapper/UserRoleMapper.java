/**
 * Copyright (C), 2019-2020
 */
package com.openusm.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openusm.web.system.model.User;
import com.openusm.web.system.model.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * 2020/2/29 22:01
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 *
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("SELECT * FROM sys_user_role WHERE USER_ID=#{user_id}")
    List<UserRole> listByUserId(@Param("userId") Long userId);

}
