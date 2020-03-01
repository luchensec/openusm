package com.openusm.web.system.controller;

import com.openusm.web.common.vo.RestResponse;
import com.openusm.web.system.model.User;
import com.openusm.web.system.service.SystemConfigService;
import com.openusm.web.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ConcurrentAccessException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/26 2:01
 */
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private SystemConfigService systemConfigService;
    /**
     * 登录方法
     * @param username 用户账号
     * @param password 用户密码
     * @return
     */
    @RequestMapping(value = "/login") //method = RequestMethod.POST
    public RestResponse userLogin(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            return RestResponse.error("用户名或密码错误", 0);
        } catch (LockedAccountException e) {
            String errorCount = systemConfigService.getByParamName("login_error_try_count")
                    .getParamValue();
            return RestResponse.error("密码输入错误" + errorCount + "次，账号被锁定，请稍后重试", 0);
        } catch (ConcurrentAccessException e) {
            return RestResponse.error("用户名或密码错误", 0);
        }

        return RestResponse.success(subject.getSession().getId(), 1);
    }

    @RequestMapping(value = "/ajaxLogin")
    public boolean saveUser() {
        User user = new User();

        user.setCtime(new Date());
        user.setUsername("secadmin");
        user.setDeptId(1L);
        user.setEmail("secadmin@openusm.com");
        user.setLastLoginTime(new Date());
        user.setMobile("138000000");
        user.setMtime(new Date());
        user.setNickname("安全管理员");
        user.setPassword("123qwe");
        user.setSalt("3c592321bb86922c7289d0b9734e1897");
        user.setStatus(1);

        userService.save(user);

        return true;
    }
}
