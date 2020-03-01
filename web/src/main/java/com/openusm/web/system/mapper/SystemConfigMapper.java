package com.openusm.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openusm.web.system.model.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

    @Select("SELECT * FROM sys_config where PARAM_NAME=#{paramName}")
    SystemConfig getByParamName(@Param("paramName") String paramName);
}
