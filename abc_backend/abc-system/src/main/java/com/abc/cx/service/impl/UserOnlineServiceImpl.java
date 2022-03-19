package com.abc.cx.service.impl;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.abc.cx.domain.User;
import com.abc.cx.domain.UserOnline;
import com.abc.cx.domain.repository.UserOnlineRepository;
import com.abc.cx.domain.repository.UserRepository;
import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.service.IUserOnlineService;
import com.abc.cx.utils.CommonUtil;
import com.abc.cx.vo.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:48 2021/11/6
 **/
@Service
public class UserOnlineServiceImpl implements IUserOnlineService {

    private UserServiceImpl userService;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    private UserOnlineRepository userOnlineRepository;

    @Autowired
    public void setUserOnlineRepository(UserOnlineRepository userOnlineRepository) {
        this.userOnlineRepository = userOnlineRepository;
    }

    private SessionRegistry sessionRegistry;

    @Autowired
    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }


    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 获取在线用户列表
     *
     * @param username 用户名
     * @param location 地址
     * @return 在线用户对象列表
     */
    @Override
    public Ret listUserOnline(String username, String location) {
        return Ret.ok("list", sessionRegistry.getAllPrincipals());
    }


    /**
     * 增加在线用户
     *
     * @param request 请求
     * @return 增加结果
     */
    @Override
    public Ret insertUserOnline(HttpServletRequest request) {
        User user = userService.getCurrentLoginUser();
        UserOnline userOnline = new UserOnline();
        userOnline.setSessionId(request.getSession().getId());
        userOnline.setUsername(user.getUsername());
        userOnline.setDeptName(user.getDept() == null ? "" : user.getDept().getName());
        userOnline.setSessionCreateTime(new Date());
        userOnline.setStatus(OperateStatusEnum.SUCCESS);
        String ip = ServletUtil.getClientIP(request);
        userOnline.setIp(ip);
        userOnline.setLocation(CommonUtil.getCityInfo(ip));
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOs().toString();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().toString();
        userOnline.setOs(os);
        userOnline.setBrowser(browser);
        userOnlineRepository.save(userOnline);
        return Ret.ok("message", "增加角色成功");
    }

    /**
     * 删除在线用户
     *
     * @param userId 用户id
     * @return 删除结果
     */
    @Override
    public Ret deleteUserOnline(Long userId) {
        User user = userRepository.findTopById(userId);
        List<SessionInformation> infos = sessionRegistry.getAllSessions(user, true);
        for (SessionInformation info : infos) {
            info.expireNow(); //expire the session
        }
        return Ret.ok("message", "删除在线用户成功");
    }

}
