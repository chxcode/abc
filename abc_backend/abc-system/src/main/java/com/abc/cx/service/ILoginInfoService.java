package com.abc.cx.service;

import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.vo.Ret;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:49 2021/11/6
 **/
public interface ILoginInfoService {

    /**
     * 获取登录信息分页列表
     *
     * @param status    状态
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param page      页码
     * @param pageSize  页数
     * @return 登录信息对象列表结果
     */
    Ret pageLoginInfos(OperateStatusEnum status, String beginDate, String endDate, Integer page, Integer pageSize);

    /**
     * 获取登录信息详细信息
     *
     * @param id 登录信息对象id
     * @return 登录信息对象详情结果
     */
    Ret getLoginInfo(Long id);

    /**
     * 新增登录信息
     *
     * @param username 登录名
     * @param status   状态
     * @param message  登录信息
     * @return 增加结果
     */
    Ret insertLoginInfo(String username, OperateStatusEnum status, String message);

}
