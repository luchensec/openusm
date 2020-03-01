package com.openusm.web.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openusm.web.system.mapper.*;
import com.openusm.web.system.model.*;
import com.openusm.web.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/23 1:53
 */
@Slf4j
@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private MenuMapper menuMapper;

    /**
     * 根据用户名查询用户角色信息
     * @param username
     * @return
     */
    @Override
    public Set<String> queryRoleNameByUsername(String username) {
        User user = this.findByUsername(username);
        List<UserRole> userRoles = userRoleMapper.listByUserId(user.getId());

        Set<String> roleNames = new HashSet<>();

        userRoles.forEach(userRole -> {
            Role role = roleMapper.selectById(userRole.getRoleId());
            roleNames.add(role.getRoleName());
        });

        return roleNames;
    }

    @Override
    public Set<String> queryPermsByUsername(String username) {
        User user = this.findByUsername(username);
        List<UserRole> userRoles = userRoleMapper.listByUserId(user.getId());

        Set<RoleMenu> roleMenuSet = new HashSet<>();

        userRoles.forEach(userRole -> {
            List<RoleMenu> roleMenus = roleMenuMapper.listByRoleId(userRole.getRoleId());
            roleMenuSet.addAll(roleMenus);
        });

        Set<String> perms = new HashSet<>();

        roleMenuSet.forEach(roleMenu -> {
            Menu menu = menuMapper.selectById(roleMenu.getMenuId());
            perms.add(menu.getPerms());
        });

        return perms;
    }

    @Override
    public User findByUsername(String username) {
        return baseMapper.getUserByUsername(username);
    }
}
