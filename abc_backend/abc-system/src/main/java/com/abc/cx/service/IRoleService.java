package com.abc.cx.service;

import com.abc.cx.domain.Role;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.vo.Ret;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 23:45 2021/11/6
 **/
public interface IRoleService {

    /**
     * 获取角色列表
     *
     * @param name      名称
     * @param status    状态
     * @param key       键值
     * @param orderNum  排序
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 角色对象列表结果
     */
    Ret listRoles(String name, EnableStatusEnum status, String key, String orderNum, String beginDate, String endDate);

    /**
     * 获取角色详细信息
     *
     * @param id 角色对象id
     * @return 角色对象详情结果
     */
    Ret getRole(Long id);

    /**
     * 新增角色
     *
     * @param role 角色对象
     * @return 增加结果
     */
    Ret insertRole(Role role);

    /**
     * 获取角色
     * @param key
     * @return Role
     */
    Role getRoleByKey(String key);

    /**
     * 修改角色
     *
     * @param role 角色对象
     * @return 修改结果
     */
    Ret updateRole(Role role);

    /**
     * 删除角色
     *
     * @param id 角色对象id
     * @return 删除结果
     */
    Ret deleteRole(Long id);

    /**
     * 批量删除角色
     *
     * @param ids 用户id列表
     * @return 批量删除结果
     */
    Ret deleteRoles(List<Long> ids);

}
