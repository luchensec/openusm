/**
 * Copyright (C), 2019-2020
 */
package com.openusm.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openusm.web.system.model.User;

import java.util.Set;

/**
 *
 *
 * @author xingfu_xiaohai@163.com
 * 2020/2/26 15:38
 * @since 1.0.0
 */
public interface UserService extends IService<User> {

    Set<String> queryRoleNameByUsername(String username);

    User findByUsername(String username);

    Set<String> queryPermsByUsername(String username);
}
