package com.abc.cx.domain.repository;

import com.abc.cx.domain.DictType;
import com.abc.cx.enums.EnableStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 字典类型
 * @Date: 22:29 2021/11/6
 **/
public interface DictTypeRepository extends JpaRepository<DictType, Long> {

    /**
     * 获取相应id的字典类型
     *
     * @param id 字典类型id
     * @return 字典类型对象
     */
    DictType findTopById(Long id);

    /**
     * 获取相应条件的字典类型列表
     *
     * @param isDelete 是否删除
     * @param status   字典类型状态
     * @return 字典类型对象列表
     */
    List<DictType> findAllByIsDeleteAndStatusOrderByCreateTimeAsc(Integer isDelete, EnableStatusEnum status);

    /**
     * 获取相应条件的字典类型分页列表
     *
     * @param specification 条件
     * @param pageable      分页
     * @return 字典类型对象分页列表
     */
    Page<DictType> findAll(Specification specification, Pageable pageable);

}
