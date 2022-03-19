package com.abc.cx.admin.controller;


import com.abc.cx.annotation.RateLimiter;
import com.abc.cx.vo.Ret;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: ChangXuan
 * @Decription: 测试接口
 * @Date: 12:45 2021/11/7
 **/
@Api(tags = "测试接口")
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping
    public String hello() {
        return "Hello JCYY";
    }

    @GetMapping("/limit")
    @RateLimiter(max = 10, timeout = 20, timeUnit = TimeUnit.SECONDS)
    public Ret limitVisit() {
        return Ret.ok().set("data", "SUCCESS");
    }
}
