package com.openusm.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openusm.web.system.model.SystemConfig;


public interface SystemConfigService extends IService<SystemConfig> {
    SystemConfig getByParamName(String paramName);
}
