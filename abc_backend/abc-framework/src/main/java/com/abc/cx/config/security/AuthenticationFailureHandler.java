package com.abc.cx.config.security;

import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.enums.ResultEnum;
import com.abc.cx.service.impl.LoginInfoServiceImpl;
import com.abc.cx.vo.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: ChangXuan
 * @Decription: 鉴权失败
 * @Date: 3:10 2021/11/7
 **/
@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private LoginInfoServiceImpl loginInfoService;

    @Autowired
    public void setLoginInfoServiceImpl(LoginInfoServiceImpl loginInfoService) {
        this.loginInfoService = loginInfoService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        loginInfoService.insertLoginInfo(request.getParameter("username"), OperateStatusEnum.FAIL, exception.getMessage());
        PrintWriter out = response.getWriter();
        try {
            out.write(Ret.failMsg(ResultEnum.LOGIN_FAILED.getMessage()).toJson());
        } catch (Exception ex) {
            out.write("序列化错误");
        } finally {
            out.flush();
            out.close();
        }
    }

}
