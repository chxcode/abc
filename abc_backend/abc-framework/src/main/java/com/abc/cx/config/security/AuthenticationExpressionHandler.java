package com.abc.cx.config.security;

import com.abc.cx.vo.Ret;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: ChangXuan
 * @Decription: 会话失效处理
 * @Date: 3:30 2021/11/7
 **/
@Component
public class AuthenticationExpressionHandler implements InvalidSessionStrategy {

    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        PrintWriter out = httpServletResponse.getWriter();
        try {
            out.write(Ret.failMsg("会话已失效").toJson());
        } catch (Exception ex) {
            out.write("序列化错误");
        } finally {
            out.flush();
            out.close();
        }
    }
}
