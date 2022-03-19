package com.abc.cx.module.captcha;

import cn.hutool.core.util.StrUtil;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * @Author: ChangXuan
 * @Decription: 验证码工厂
 * @Date: 13:45 2021/3/9
 **/
@Slf4j
@Component
public class CaptchaFactory {

    private final DefaultCaptchaProperties defaultCaptchaProperties = new DefaultCaptchaProperties();

    public Captcha create(CaptchaEnum type) throws Exception {
        defaultCaptchaProperties.setCodeType(type);
        return switchCaptcha(defaultCaptchaProperties);
    }

    public DefaultCaptchaProperties getDefaultCaptchaProperties(){
        return this.defaultCaptchaProperties;
    }

    private Captcha switchCaptcha(DefaultCaptchaProperties captchaProperties) throws Exception {
        Captcha captcha;
        synchronized (this) {
            switch (captchaProperties.getCodeType()) {
                case ARITHMETIC:
                    // 算术类型 https://gitee.com/whvse/EasyCaptcha
                    captcha = new FixedArithmeticCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight());
                    // 几位数运算，默认是两位
                    captcha.setLen(captchaProperties.getLength());
                    break;
                case CHINESE:
                    captcha = new ChineseCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight());
                    captcha.setLen(captchaProperties.getLength());
                    break;
                case CHINESE_GIF:
                    captcha = new ChineseGifCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight());
                    captcha.setLen(captchaProperties.getLength());
                    break;
                case GIF:
                    captcha = new GifCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight());
                    captcha.setLen(captchaProperties.getLength());
                    break;
                case SPEC:
                    captcha = new SpecCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight());
                    captcha.setLen(captchaProperties.getLength());
                    break;
                default:
                    throw new Exception("验证码配置信息错误！正确配置查看 DefaultCaptchaProperties ");
            }
        }
        if(StrUtil.isNotBlank(captchaProperties.getFontName())){
            captcha.setFont(new Font(captchaProperties.getFontName(), Font.PLAIN, captchaProperties.getFontSize()));
        }
        return captcha;
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            // 生成随机数字和运算符
            int n1 = num(1, 10), n2 = num(1, 10);
            int opt = num(3);
            // 交换值避免产生负值结果
            if (n1 < n2) {
                n1 ^= n2;
                n2 ^= n1;
                n1 ^= n2;
            }
            // 计算结果
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            // 转换为字符运算符
            char optChar = "+-x".charAt(opt);

            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);

            return chars.toCharArray();
        }
    }
}
