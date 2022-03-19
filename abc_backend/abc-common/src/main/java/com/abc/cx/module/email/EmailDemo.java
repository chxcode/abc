package com.abc.cx.module.email;

import cn.hutool.core.io.resource.ResourceUtil;
import org.jasypt.encryption.StringEncryptor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.net.URL;

/**
 * @Author: ChangXuan
 * @Decription: 邮件服务使用 Demo
 * @Date: 13:57 2021/3/16
 **/
public class EmailDemo {

    private StringEncryptor encryptor;

    private MailService mailService;

    private TemplateEngine templateEngine;

    /**
     * 生成配置文件中的加密密文
     */
    public void generatePassword() {
        // 你的邮箱密码
        String password = "password!";
        // 加密后的密码(注意：配置上去的时候需要加 ENC(加密密码))
        String encryptPassword = encryptor.encrypt(password);
        String decryptPassword = encryptor.decrypt(encryptPassword);

        System.out.println("password = " + password);
        System.out.println("encryptPassword = " + encryptPassword);
        System.out.println("decryptPassword = " + decryptPassword);
    }


    /**
     * 测试简单邮件
     */
    public void sendSimpleMail() {
        mailService.sendSimpleMail(
                "chang-xuan@qq.com",
                "一封纯文本邮件",
                "这是一封普通的纯文本测试邮件 by jcyy_backend");
    }

    /**
     * 测试HTML邮件
     *
     * @throws MessagingException 邮件异常
     */
    public void sendHtmlMail() throws MessagingException {
        Context context = new Context();
        context.setVariable("project", "Spring Boot Email Demo");
        context.setVariable("author", "JCYY_BACKEND");
        context.setVariable("url", "http://www.int-yt.com");
        // welcome 为 resources/templates 路径下的 thymeleaf 模板文件
        String emailTemplate = templateEngine.process("welcome", context);
        mailService.sendHtmlMail("chang-xuan@qq.com", "这是一封模板HTML邮件", emailTemplate);
    }


    /**
     * 测试附件邮件
     *
     * @throws MessagingException 邮件异常
     */
    public void sendAttachmentsMail() throws MessagingException {
        // attachment.png 为 resources/static 路径下的 资源文件
        URL resource = ResourceUtil.getResource("static/attachment.png");
        mailService.sendAttachmentsMail("chang-xuan@qq.com", "这是一封带附件的邮件", "邮件中有附件，请注意查收！", resource.getPath());
    }


}
