package com.abc.cx.module.captcha;

import cn.hutool.core.util.StrUtil;
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
 * @Decription: 核验验证码
 * @Date: 15:11 2021/3/9
 **/
@Slf4j
@Aspect
@Component
public class CaptchaAspect {

    private RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.abc.cx.module.captcha.VerifyCaptcha)")
    public void verifyPointCut() {

    }

    @Around("verifyPointCut()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String captcha = request.getParameter("captcha");
        String captchaId = request.getParameter("captchaId");
        String cacheCode = redisUtil.getString(captchaId);
        redisUtil.del(captchaId);
        if (StrUtil.isBlank(cacheCode)) {
            throw new RuntimeException("验证码不存在或已过期");
        }
        if (StrUtil.isBlank(captcha) || !StrUtil.equalsIgnoreCase(captcha, cacheCode)) {
            throw new RuntimeException("验证码错误");
        }
        return point.proceed();
    }
}
