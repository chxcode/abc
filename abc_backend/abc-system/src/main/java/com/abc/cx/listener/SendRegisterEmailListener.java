package com.abc.cx.listener;

import cn.hutool.core.util.IdUtil;
import com.abc.cx.config.Configure;
import com.abc.cx.domain.User;
import com.abc.cx.events.UserRegisterEvent;
import com.abc.cx.module.email.MailService;
import com.abc.cx.utils.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;


/**
 * @Author: ChangXuan
 * @Decription: 发送注册验证邮件监听器
 * @Date: 11:12 2021/3/15
 **/
@Component
@EnableAsync
@Slf4j
public class SendRegisterEmailListener implements ApplicationListener<UserRegisterEvent> {

    private static final String VERIFY_EMAIL_KEY = "verify:email:key:";

    private RedisUtil redisUtil;

    private TemplateEngine templateEngine;

    private MailService mailService;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    private Configure configure;

    @Autowired
    public void setConfigure(Configure configure) {
        this.configure = configure;
    }

    @SneakyThrows
    @Override
    @Async
    public void onApplicationEvent(UserRegisterEvent event) {
        User user = event.getUser();
        HttpServletRequest request = event.getHttpServletRequest();
        String uuid = IdUtil.simpleUUID();
        String code = VERIFY_EMAIL_KEY + uuid;
        redisUtil.setString(code, user.getId().toString(), 600);
        Context context = new Context();
        context.setVariable("userName", user.getUsername());
        context.setVariable("url", configure.getFrontendAddress() + "/passport/account-verify/" + uuid);
        String emailTemplate = templateEngine.process("account-activate", context);
        mailService.sendHtmlMail(user.getEmail(), "账号激活", emailTemplate);
    }
}
