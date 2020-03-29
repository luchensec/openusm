/**
 * Copyright (C), 2019-2020
 */
package com.openusm.web.system.controller;

import com.openusm.common.constant.Constants;
import com.openusm.common.util.IdUtils;
import com.openusm.web.common.redis.RedisService;
import com.openusm.web.common.util.VerifyCodeUtils;
import com.openusm.web.common.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *
 * 2020/3/28 18:46
 * @author xingfu_xiaohai@163.com
 * @since 1.0.0
 *
 */
@Slf4j
@RestController
public class VerifyCodeController {
    @Autowired
    private RedisService redisService;
    /**
     * 生成验证码
     */
    @GetMapping("/generateVerifyCode")
    public R genVerifyCodeImage(HttpServletResponse response) throws IOException
    {
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        // 唯一标识
        String csrftoken = IdUtils.simpleUUID();
        String verifyKey = Constants.VERIFY_CODE_KEY + csrftoken;

        redisService.setCacheObject(verifyKey, verifyCode, Constants.VERIFY_CODE_EXPIRATION, TimeUnit.MINUTES);
        // 生成图片
        int w = 111, h = 36;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(w, h, stream, verifyCode);
        try
        {
            R ret = R.ok();
            ret.data("csrftoken", csrftoken);
            ret.data("img", Base64.encode(stream.toByteArray()));
            return ret;
        }
        catch (Exception e)
        {
            log.error("生成验证码出错:", e);
            return R.error().message(e.getMessage());
        }
        finally
        {
            stream.close();
        }
    }
}
