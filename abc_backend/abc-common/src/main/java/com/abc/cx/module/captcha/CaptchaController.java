package com.abc.cx.module.captcha;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.abc.cx.utils.RedisUtil;
import com.abc.cx.vo.Ret;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 14:05 2021/3/9
 **/
@Api(tags = "验证码")
@Slf4j
@RestController
@RequestMapping(value = "/auth/captcha")
public class CaptchaController {

    private static final String CODE_KEY = "code:key:";

    private CaptchaFactory captchaFactory;

    private RedisUtil redisUtil;

    @Autowired
    public void setCaptchaFactory(CaptchaFactory captchaFactory) {
        this.captchaFactory = captchaFactory;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @ApiOperation(value = "获取验证码", notes = "默认是算术验证码，获取其它类型验证码可根据需要传所需类型")
    @ApiImplicitParam(
            name = "type",
            value = "验证码类型[算术, 中文, 中文动图, 动图, 特殊]",
            dataType = "CaptchaEnum",
            paramType = "query")
    @GetMapping("/get")
    public Ret getCaptchaController(@RequestParam(defaultValue = "ARITHMETIC") CaptchaEnum type) throws Exception {
        Captcha captcha = captchaFactory.create(type);
        String uuid = CODE_KEY + IdUtil.simpleUUID();
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == CaptchaEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtil.setString(uuid, captchaValue, captchaFactory.getDefaultCaptchaProperties().getExpiration());
        return Ret.ok().set("img", captcha.toBase64()).set("uuid", uuid);
    }

    @ApiOperation("演示：验证验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "captcha", value = "验证码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "captchaId", value = "UUID", dataType = "String", paramType = "query")
    })
    @GetMapping("/verify")
    public Ret verify(@RequestParam(defaultValue = "") String captcha, @RequestParam(defaultValue = "") String captchaId) {
        String cacheCode = redisUtil.getString(captchaId);
        redisUtil.del(captchaId);
        if (StrUtil.isBlank(cacheCode)) {
            return Ret.failMsg("验证码不存在或已过期");
        }
        if (StrUtil.isBlank(captcha) || !StrUtil.equalsIgnoreCase(captcha, cacheCode)) {
            return Ret.failMsg("验证码错误");
        }
        return Ret.ok("msg", "验证码正确");
    }

    @ApiOperation(
            value = "演示：使用注解自动核验验证码",
            notes = "前端同学传递参数名称使用 captcha 和 captchaId, 后端使用 @VerifyCaptcha 可以自动核验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "captcha", value = "验证码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "captchaId", value = "UUID", dataType = "String", paramType = "query")
    })
    @VerifyCaptcha
    @GetMapping("/auto/verify")
    public Ret autoVerify(@RequestParam(defaultValue = "") String captcha, @RequestParam(defaultValue = "") String captchaId) {
        log.info("success!!! 验证码：{}， 验证码ID：{}", captcha, captchaId);
        return Ret.ok("msg", "成功访问资源");
    }

}
