package com.openusm.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openusm.web.system.model.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 根据角色ID查询权限信息
     * @param roleId 角色ID
     * @return 权限信息
     */
    @Select("SELECT * FROM sys_role_menu WHERE role_id=#{roleId}")
    List<RoleMenu> listByRoleId(@Param("roleId") Long roleId);
}
