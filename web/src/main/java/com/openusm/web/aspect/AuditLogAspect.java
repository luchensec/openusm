package com.openusm.web.aspect;

import com.openusm.web.annotation.Audit;
import com.openusm.web.system.model.AuditLog;
import com.openusm.web.system.model.User;
import com.openusm.web.system.service.AuditLogService;
import com.openusm.web.util.IpUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author xingfu_xiaohai@163.com
 * @since 2019/9/22 18:36
 */

@Aspect
@Component
public class AuditLogAspect {
    @Autowired
    private AuditLogService auditLogService;

    @Pointcut("@annotation(com.openusm.web.annotation.Audit)")
    public void logPointCut() { }

    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String clientIp = IpUtils.getIpAddr(request);
        String username = ((User) SecurityUtils.getSubject().getPrincipal()).getUsername();

        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        // 获得注解的操作日志类型
        Audit auditLog = method.getAnnotation(Audit.class);

        String operation = auditLog.operation();
        String module = auditLog.module();
        String message = auditLog.message();

        AuditLog log = new AuditLog();

        log.setClientIp(clientIp);
        log.setCtime(new Date());
        log.setFunModule(module);
        log.setMethod(method.getName());
        log.setOperation(operation);
        log.setUsername(username);
        //请求的参数
        String args = joinPoint.getArgs()[0].toString();
        log.setParams(args);
        log.setResult(1);
        log.setMessage(message);

        auditLogService.save(log);
    }

    @AfterThrowing(pointcut = "logPointCut()")
    public void doAfter(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String clientIp = IpUtils.getIpAddr(request);
        String username = ((User) SecurityUtils.getSubject().getPrincipal()).getUsername();

        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        // 获得注解的操作日志类型
        Audit auditLog = method.getAnnotation(Audit.class);

        String operation = auditLog.operation();
        String module = auditLog.module();
        String message = auditLog.message();

        AuditLog log = new AuditLog();

        log.setClientIp(clientIp);
        log.setCtime(new Date());
        log.setFunModule(module);
        log.setMethod(method.getName());
        log.setOperation(operation);
        log.setUsername(username);
        //请求的参数
        String args = joinPoint.getArgs()[0].toString();
        log.setParams(args);
        log.setResult(0);
        log.setMessage(message);

        auditLogService.save(log);
    }

}
