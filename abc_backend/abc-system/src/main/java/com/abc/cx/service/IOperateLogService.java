package com.abc.cx.service;

import com.abc.cx.domain.OperateLog;
import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.vo.Ret;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:58 2021/11/6
 **/
public interface IOperateLogService {

    /**
     * 获取操作日志分页列表
     *
     * @param title     标题
     * @param type      类型
     * @param status    操作状态
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param page      页码
     * @param pageSize  页数
     * @return 操作日志对象列表结果
     */
    Ret pageOperateLogs(String title, OperateTypeEnum type, OperateStatusEnum status, String beginDate, String endDate, Integer page, Integer pageSize);

    /**
     * 获取操作日志详情
     *
     * @param id 操作日志对象id
     * @return 操作日志对象详情结果
     */
    Ret getOperateLog(Long id);

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     * @return 增加结果
     */
     void insertOperateLog(OperateLog operLog);

    /**
     * 删除操作日志
     *
     * @param id 操作日志对象id
     * @return 删除结果
     */
    Ret deleteOperateLog(Long id);

}
