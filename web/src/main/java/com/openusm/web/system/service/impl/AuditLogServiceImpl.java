package com.openusm.web.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openusm.web.system.mapper.AuditLogMapper;
import com.openusm.web.system.model.AuditLog;
import com.openusm.web.system.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/22 22:39
 */
@Transactional
@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogService {

}
