package com.abc.cx.service;

import com.abc.cx.domain.DictData;
import com.abc.cx.vo.Ret;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:48 2021/11/6
 **/
public interface IDictDataService {

    /**
     * 获取字典数据列表
     *
     * @param type  类型
     * @param label 标签
     * @return 字典数据对象列表结果
     */
    Ret listDictData(String type, String label);

    /**
     * 根据条件获取字典数据列表
     *
     * @param name     字典名称
     * @param page     页面
     * @param pageSize 页数
     * @return 字典数据对象列表结果
     */
    Ret pageDictDatas(String name, Integer page, Integer pageSize);

    /**
     * 获取字典数据详细信息
     *
     * @param id 字典数据id
     * @return 字典数据详情结果
     */
    Ret getDictData(Long id);

    /**
     * 新增字典数据
     *
     * @param dictData 字典数据对象
     * @return 增加结果
     */
    Ret insertDictData(DictData dictData);

    /**
     * 修改字典数据
     *
     * @param dictData 字典数据对象
     * @return 修改结果
     */
    Ret updateDictData(DictData dictData);

    /**
     * 删除字典数据
     *
     * @param id 字典数据id
     * @return 删除结果
     */
    Ret deleteDictData(Long id);

}
