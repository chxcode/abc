package com.abc.cx.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: Get 请求参数处理
 * @Date: 2:29 2021/11/7
 **/
@Slf4j
@Aspect
@Component
public class GetParamHandlerAspect {

    @Pointcut("execution(* *..controller.*.list*(..))")
    public void getPointCut() {
    }

    @Around("getPointCut()")
    public Object doProcess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //捕获方法参数列表
        Object[] methodArgs = proceedingJoinPoint.getArgs();
        List<Object> list = new ArrayList<>();
        //循环所有参数
        for (Object arg : methodArgs) {
            if (arg == null) {
                list.add(null);
                continue;
            }
            //判断是否是String,如果是，对%和_进行特殊处理
            if (arg instanceof String) {
                arg = arg.toString().replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
            }
            list.add(arg);
        }
        return proceedingJoinPoint.proceed(list.toArray());
    }

}
