package com.openusm.web.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openusm.web.system.mapper.SystemConfigMapper;
import com.openusm.web.system.model.SystemConfig;
import com.openusm.web.system.service.SystemConfigService;
import org.springframework.stereotype.Service;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/26 22:52
 */
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig>
        implements SystemConfigService {

    @Override
    public SystemConfig getByParamName(String paramName) {
        return baseMapper.getByParamName(paramName);
    }
}
