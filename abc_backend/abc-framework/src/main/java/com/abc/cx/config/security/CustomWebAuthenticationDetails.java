package com.abc.cx.config.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: ChangXuan
 * @Decription: 封装验证码信息
 * @Date: 16:27 2021/3/22
 **/
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private static final long serialVersionUID = 3982955176291533865L;

    /**
     * 用户输入的验证码
     */
    public static final String FIELD_FORM_CAPTCHA_KEY = "code";
    /**
     * 验证码所对应的 id
     */
    public static final String FIELD_FORM_UUID_KEY = "uuid";

    private final String code;
    private final String uuid;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        uuid = request.getParameter(FIELD_FORM_UUID_KEY);
        code = request.getParameter(FIELD_FORM_CAPTCHA_KEY);
    }

    public String getCode() {
        return code;
    }

    public String getUuid() {
        return uuid;
    }


}
