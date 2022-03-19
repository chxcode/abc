package com.abc.cx.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.abc.cx.annotation.Log;
import com.abc.cx.domain.OperateLog;
import com.abc.cx.domain.User;
import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.service.impl.OperateLogServiceImpl;
import com.abc.cx.service.impl.UserServiceImpl;
import com.abc.cx.utils.CommonUtil;
import com.abc.cx.vo.Ret;
import com.abc.cx.wrapper.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: ChangXuan
 * @Decription: 日志记录
 * @Date: 2:39 2021/11/7
 **/
@Slf4j
@Aspect
@Component
public class LogHandlerAspect {

    private UserServiceImpl userService;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    private OperateLogServiceImpl operateLogService;

    @Autowired
    public void setOperateLogServiceImpl(OperateLogServiceImpl operateLogService) {
        this.operateLogService = operateLogService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.abc.cx.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 返回后的通知 用于拦截操作
     *
     * @param joinPoint 切入点
     */
    @AfterReturning(returning = "ret", pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint, Ret ret) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        handleLog(joinPoint, request, ret);
    }

    private void handleLog(final JoinPoint joinPoint, HttpServletRequest request, Ret ret) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            // 获取当前的用户
            User currentUser = userService.getCurrentLoginUser();
            // *========操作日志=========*//
            OperateLog operateLog = new OperateLog();
            operateLog.setStatus(OperateStatusEnum.SUCCESS);
            operateLog.setUrl(request.getRequestURI());
            // 请求的地址
            String ip = ServletUtil.getClientIP(request);
            operateLog.setIp(ip);
            operateLog.setLocation(CommonUtil.getCityInfo(ip));
            if (currentUser != null) {
                operateLog.setOperator(currentUser.getRealName());
                if (!StringUtils.isEmpty(currentUser.getDept()) && !StringUtils.isEmpty(currentUser.getDept().getName())) {
                    operateLog.setDeptName(currentUser.getDept().getName());
                }
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operateLog.setMethod(className + "." + methodName + "()");
            // 处理设置注解上的参数
            // 设置action动作
            operateLog.setType(controllerLog.operateType());
            // 设置标题
            operateLog.setTitle(controllerLog.title());
            if (ret.isFail()) {
                operateLog.setStatus(OperateStatusEnum.FAIL);
                operateLog.setErrorMsg(ret.getStr("msg"));
            }
            Map pathVars = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            String param = JSONUtil.toJsonStr(pathVars);
            RequestWrapper requestWrapper = new RequestWrapper(request);
            String body = requestWrapper.getBody();
            String method = request.getMethod();
            if (StrUtil.equalsIgnoreCase(method, RequestMethod.POST.name()) || StrUtil.equalsIgnoreCase(method, RequestMethod.PUT.name())) {
                operateLog.setParam(body);
                log.info("前端参数：" + body);
            } else {
                operateLog.setParam(param);
                log.info("前端参数：" + param);
            }
            operateLogService.insertOperateLog(operateLog);
        } catch (Exception e) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }
}
