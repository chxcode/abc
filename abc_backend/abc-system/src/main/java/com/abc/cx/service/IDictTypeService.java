package com.abc.cx.service;

import com.abc.cx.domain.DictType;
import com.abc.cx.vo.Ret;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:55 2021/11/6
 **/
public interface IDictTypeService {

    /**
     * 获取字典类型列表
     *
     * @return 字典类型列表结果
     */
    Ret listDictTypes();

    /**
     * 获取字典类型分页列表
     *
     * @param name     名称
     * @param type     类型
     * @param page     页码
     * @param pageSize 页数
     * @return 字典类型列表结果
     */
    Ret pageDictTypes(String name, String type, Integer page, Integer pageSize);

    /**
     * 获取字典类型详细信息
     *
     * @param id 字典类型id
     * @return 字典类型详情结果
     */
    Ret getDictType(Long id);

    /**
     * 新增字典类型
     *
     * @param dictType 字典类型对象
     * @return 增加结果
     */
    Ret insertDictType(DictType dictType);

    /**
     * 修改字典类型
     *
     * @param dictType 字典类型对象
     * @return 修改结果
     */
    Ret updateDictType(DictType dictType);

    /**
     * 删除字典类型
     *
     * @param id 字典类型id
     * @return 删除结果
     */
    Ret deleteDictType(Long id);

}
