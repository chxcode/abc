package com.abc.cx.aspect;

import com.abc.cx.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:01 2021/11/7
 **/
@Slf4j
@Aspect
@Component
public class IdempotentAspect {

    private static final String CODE_BUCKET = "IDEMPOTENT:CODE_BUCKET:";

    private static final String HEADER_VERIFY_NAME = "X-CXV-TOKEN";

    private RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.abc.cx.annotation.Idempotent)")
    public void idempotentPointCut() {
    }

    @Around("idempotentPointCut()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        if (redisUtil.del(CODE_BUCKET + request.getHeader(HEADER_VERIFY_NAME)) == 0) {
            // 失败
            throw new RuntimeException("请求失效，请重试！");
        }
        return point.proceed();
    }

}
