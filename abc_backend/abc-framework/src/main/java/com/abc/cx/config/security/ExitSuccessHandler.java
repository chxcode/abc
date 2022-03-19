package com.abc.cx.config.security;

import com.abc.cx.domain.User;
import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.service.impl.LoginInfoServiceImpl;
import com.abc.cx.vo.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: ChangXuan
 * @Decription: 退出成功
 * @Date: 4:19 2021/11/7
 **/
@Component
public class ExitSuccessHandler implements LogoutSuccessHandler {

    private LoginInfoServiceImpl loginInfoService;

    @Autowired
    public void setLoginInfoServiceImpl(LoginInfoServiceImpl loginInfoService) {
        this.loginInfoService = loginInfoService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Cookie[] cookies = request.getCookies();
        Cookie sig = readCookie(cookies, "URAS-TGT.sig");
        Cookie tgt = readCookie(cookies, "URAS-TGT");
        setCookieExpired(sig, response);
        setCookieExpired(tgt, response);
        User user = (User) authentication.getPrincipal();
        request.getSession().invalidate();
        loginInfoService.insertLoginInfo(user.getUsername(), OperateStatusEnum.SUCCESS, "登出成功");
        PrintWriter out = response.getWriter();
        try {
            out.write(Ret.ok("message", "登出成功").toJson());
        } catch (Exception e) {
            e.printStackTrace();
            out.write("序列化错误");
        } finally {
            out.flush();
            out.close();
        }
    }

    private Cookie readCookie(Cookie[] cookies, String name) {
        if (cookies != null && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    return c;
                }
            }
        }
        return null;
    }

    private void setCookieExpired(Cookie cookie, HttpServletResponse response) {
        if (cookie == null) {
            return;
        }
        cookie.setMaxAge(0);
        cookie.setDomain("lunjian.team");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
