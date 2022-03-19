package com.abc.cx.service;

import com.abc.cx.vo.Ret;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:45 2021/11/6
 **/
public interface IUserOnlineService {

    /**
     * 获取在线用户列表
     *
     * @param username 用户名
     * @param location 登录地址
     * @return 在线用户对象列表
     */
    Ret listUserOnline(String username, String location);

    /**
     * 增加在线用户
     *
     * @param request 请求
     * @return 增加结果
     */
    Ret insertUserOnline(HttpServletRequest request);

    /**
     * 删除在线用户
     *
     * @param userId 用户id
     * @return 删除结果
     */
    Ret deleteUserOnline(Long userId);

}
