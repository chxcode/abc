package com.abc.cx.config.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author: ChangXuan
 * @Decription: Thrown if an authentication request is rejected because the captcha are invalid.
 * For this exception to be thrown, it means the user need send new request try again.
 * @Date: 13:45 2021/3/24
 **/
public class BadCaptchaException extends AuthenticationException {

    private static final long serialVersionUID = -4958163526711272208L;

    /**
     * Constructs a <code>BadCaptchaException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public BadCaptchaException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>BadCaptchaException</code> with the specified message and
     * root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public BadCaptchaException(String msg, Throwable t) {
        super(msg, t);
    }

}
