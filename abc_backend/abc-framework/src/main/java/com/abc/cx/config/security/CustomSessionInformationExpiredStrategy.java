package com.abc.cx.config.security;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: ChangXuan
 * @Decription: 自定义session处理
 * @Date: 12:29 2021/11/7
 **/
@Component
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy{

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) {
        HttpServletRequest request = event.getRequest();
        HttpSession session = request.getSession();
        session.invalidate();
    }

}
