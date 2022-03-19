package com.abc.cx.service;

import com.abc.cx.domain.Param;
import com.abc.cx.vo.Ret;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 10:25 2020/5/8
 **/
public interface IParamService {

    /**
     * 获取参数分页列表
     *
     * @param name     名称
     * @param key      键值
     * @param page     页码
     * @param pageSize 页数
     * @return 参数对象列表结果
     */
    Ret pageParams(String name, String key, Integer page, Integer pageSize);

    /**
     * 获取参数详情
     *
     * @param id 参数对象id
     * @return 参数对象详情结果
     */
    Ret getParam(Long id);

    /**
     * 新增参数
     *
     * @param param 参数对象
     * @return 增加结果
     */
    Ret insertParam(Param param);

    /**
     * 修改参数
     *
     * @param param 参数对象
     * @return 修改结果
     */
    Ret updateParam(Param param);

    /**
     * 删除参数
     *
     * @param id 参数对象id
     * @return 删除结果
     */
    Ret deleteParam(Long id);

    /**
     * 根据 key 值获取参数记录
     * @param key
     * @return
     */
    Param findParamByKey(String key);

}
