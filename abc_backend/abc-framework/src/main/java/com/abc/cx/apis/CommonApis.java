package com.abc.cx.apis;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.abc.cx.annotation.RateLimiter;
import com.abc.cx.utils.RedisUtil;
import com.abc.cx.vo.Ret;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: ChangXuan
 * @Decription: 通用 API
 * @Date: 16:05 2021/3/4
 **/

@Slf4j
@Api(tags = "通用 API")
@RestController
@RequestMapping("/api/common")
public class CommonApis {

    private static final String CODE_BUCKET = "IDEMPOTENT:CODE_BUCKET:";

    private RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @ApiOperation(value = "获取幂等性请求验证码", notes = "建议项目中用户登录后才允许调用此接口")
    @GetMapping(value = "/getIdempotentCode")
    @RateLimiter(max = 1, timeUnit = TimeUnit.SECONDS)
    public Ret getIdempotentCode(){
        String code = RandomUtil.randomString(6);
        redisUtil.setString(CODE_BUCKET + code, "", 3600);
        return Ret.ok().set("code", code);
    }

    @ApiOperation(value = "获取服务器时间戳（秒）")
    @GetMapping(value = "/timestamp")
    public Ret getTimeStamp() {
        return Ret.ok("timestamp", DateUtil.currentSeconds());
    }

}
