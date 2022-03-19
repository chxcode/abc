package com.abc.cx.service;

import com.abc.cx.domain.User;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.enums.GenderTypeEnum;
import com.abc.cx.vo.Ret;
import com.abc.cx.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:25 2021/11/6
 **/
public interface IUserService {

    /**
     * 获取当前用户
     *
     * @return 用户对象信息
     */
    User getCurrentLoginUser();

    /**
     * 修改当前用户密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    Ret updatePassword(String oldPassword, String newPassword);

    /**
     * 获取用户分页列表
     *
     * @param username  用户名
     * @param realName  姓名
     * @param telephone 手机
     * @param gender    性别
     * @param status    状态
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param deptId    部门id
     * @param page      页码
     * @param pageSize  页数
     * @return 用户列表结果
     */
    Ret pageUsers(String username, String realName, String telephone, GenderTypeEnum gender, EnableStatusEnum status, String beginDate, String endDate, Long deptId, Integer page, Integer pageSize);


    Ret pageUsersPro(String username, String realName, String telephone, GenderTypeEnum gender, EnableStatusEnum status, String beginDate, String endDate, Long deptId, Integer page, Integer pageSize, List<Long> ids, String name);

    /**
     * 获取用户详情
     *
     * @param id 用户id
     * @return 用户对象结果
     */
    Ret getUser(Long id);

    /**
     * 增加用户
     *
     * @param userVO 用户VO对象
     * @return 增加结果
     */
    Ret insertUser(UserVO userVO);

    /**
     * 用户注册
     * @param userVO 用户VO对象
     * @return 注册结果
     */
    Ret register(UserVO userVO, HttpServletRequest httpServletRequest);

    /**
     * 用户名是否存在
     * @param userName 用户名
     * @return 检查结果
     */
    Ret userNameIsExit(String userName);

    /**
     * 邮箱是否已存在
     * @param email 邮箱
     * @return 检查结果
     */
    Ret emailIsExit(String email);

    /**
     * 账号激活
     * @param key key
     * @return 激活结果
     */
    Ret accountVerify(String key);

    /**
     * 修改用户
     *
     * @param userVO 用户VO对象
     * @return 修改结果
     */
    Ret updateUser(UserVO userVO);

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 删除结果
     */
    Ret deleteUser(Long id);


    /**
     * 获取用户菜单
     *
     * @param id 用户id
     * @return 菜单列表结果
     */
    Ret getMenuList(Long id);


    /**
     * 批量删除用户
     *
     * @param ids 用户id列表
     * @return 批量删除结果
     */
    Ret deleteUsers(List<Long> ids);

    /**
     * 找回密码
     * 会发送一封包含重置密码链接的邮件到邮箱
     * @param email 注册邮箱
     * @return 结果
     */
    Ret retrieve(String email, HttpServletRequest request);

    /**
     * 重置密码
     * @param uuid uuid
     * @param pwd 新密码
     * @return 重置结果
     */
    Ret resetPwd(String uuid, String pwd);
}
