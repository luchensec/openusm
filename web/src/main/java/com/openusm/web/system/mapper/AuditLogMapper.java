package com.openusm.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openusm.web.system.model.AuditLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuditLogMapper extends BaseMapper<AuditLog> {
}
