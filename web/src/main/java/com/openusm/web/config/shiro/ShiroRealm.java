/**
 * Copyright (C), 2019-2020
 */
package com.openusm.web.config.shiro;

import com.openusm.web.system.model.User;
import com.openusm.web.system.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 *
 *
 * @author xingfu_xiaohai@163.com
 * @create 2020/2/26 15:24
 * @since 1.0.0
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    public String getName() {
        return "shiroRealm";
    }


    /**
     * 当前登录的Subject授予角色和权限,访问权限资源时通过该方法，Shiro会cache一段时间权限信息，
     * 需要启用AuthorizationCache
     * @param principalCollection
     * @return 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        Set<String> roles = userService.queryRoleNameByUsername(username);
        authorizationInfo.setRoles(roles);

        Set<String> perms = userService.queryPermsByUsername(username);
        authorizationInfo.setStringPermissions(perms);

        return authorizationInfo;
    }

    /**
     * 用户认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userService.findByUsername(username);

        if (null == user) {
            throw new UnknownAccountException("帐号不存在");
        }

        if (0 == user.getStatus()) {
            throw new LockedAccountException("帐号被锁定");
        }

        Set<String> roles = userService.queryRoleNameByUsername(username);

        if (roles.isEmpty()) {
            throw new ConcurrentAccessException("没有权限");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()), getName());

        return authenticationInfo;
    }
}
