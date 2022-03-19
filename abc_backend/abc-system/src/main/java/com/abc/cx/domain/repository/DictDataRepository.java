package com.abc.cx.domain.repository;

import com.abc.cx.domain.DictData;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 字典数据
 * @Date: 22:29 2021/11/6
 **/
public interface DictDataRepository extends JpaRepository<DictData, Long> {

    /**
     * 获取相应id的字典数据
     *
     * @param id 字典数据id
     * @return 字典数据对象
     */
    DictData findTopById(Long id);

    /**
     * 获取相应条件的字典数据列表
     *
     * @param specification 条件
     * @param sort          排序
     * @return 字典数据对象列表
     */
    List<DictData> findAll(Specification specification, Sort sort);

}
