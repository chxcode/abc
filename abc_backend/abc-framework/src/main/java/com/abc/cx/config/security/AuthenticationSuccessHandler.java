package com.abc.cx.config.security;

import com.abc.cx.domain.User;
import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.service.impl.LoginInfoServiceImpl;
import com.abc.cx.service.impl.UserOnlineServiceImpl;
import com.abc.cx.vo.Ret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: ChangXuan
 * @Decription: 鉴权成功
 * @Date: 4:10 2021/11/7
 **/
@Component
@Slf4j
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private LoginInfoServiceImpl loginInfoService;

    @Autowired
    public void setLoginInfoServiceImpl(LoginInfoServiceImpl loginInfoService) {
        this.loginInfoService = loginInfoService;
    }

    private UserOnlineServiceImpl userOnlineService;

    @Autowired
    public void setUserOnlineServiceImpl(UserOnlineServiceImpl userOnlineService) {
        this.userOnlineService = userOnlineService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        try {
            User user = (User) authentication.getPrincipal();
            request.getSession().setAttribute("user", user);
            loginInfoService.insertLoginInfo(user.getUsername(), OperateStatusEnum.SUCCESS, "登录成功");
            userOnlineService.insertUserOnline(request);
            out.write(Ret.ok("message", "登录成功").set("user", user).toJson());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("[登录成功]但是, 出现错误");
            try {
                out.write(Ret.failMsg("登录失败").toJson());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            out.flush();
            out.close();
        }
    }

}
