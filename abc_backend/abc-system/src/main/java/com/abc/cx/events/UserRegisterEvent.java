package com.abc.cx.events;

import com.abc.cx.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: ChangXuan
 * @Decription: 用户注册事件
 * @Date: 11:04 2021/3/15
 **/
@Getter
public class UserRegisterEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = -5394089736148187419L;

    private final User user;

    private final HttpServletRequest httpServletRequest;

    private UserRegisterEvent(ApplicationContext applicationContext, User user, HttpServletRequest httpServletRequest) {
        super(applicationContext);
        this.user = user;
        this.httpServletRequest = httpServletRequest;
    }

    public static UserRegisterEvent create(ApplicationContext applicationContext, User user, String password, HttpServletRequest httpServletRequest) {
        return new UserRegisterEvent(applicationContext, user, httpServletRequest);
    }

}
