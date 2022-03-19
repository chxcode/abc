package com.abc.cx.module.captcha;

import lombok.Data;

/**
 * @Author: ChangXuan
 * @Decription: 验证码默认配置
 * @Date: 11:14 2021/3/9
 **/
@Data
public class DefaultCaptchaProperties {

    /**
     * 验证码配置
     */
    protected CaptchaEnum codeType;
    /**
     * 验证码有效期 秒
     */
    protected Long expiration = 120L;
    /**
     * 验证码内容长度
     */
    protected int length = 2;
    /**
     * 验证码宽度
     */
    protected int width = 111;
    /**
     * 验证码高度
     */
    protected int height = 36;
    /**
     * 验证码字体
     */
    protected String fontName;
    /**
     * 字体大小
     */
    protected int fontSize = 25;

    public CaptchaEnum getCodeType() {
        return codeType;
    }

}
