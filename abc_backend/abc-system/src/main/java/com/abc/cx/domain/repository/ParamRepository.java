package com.abc.cx.domain.repository;

import com.abc.cx.domain.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: ChangXuan
 * @Decription: 参数
 * @Date: 22:29 2021/11/6
 **/
public interface ParamRepository extends JpaRepository<Param, Long> {

    /**
     * 获取相应key的参数
     * @return
     */
    Param findTopByKeyAndIsDelete(String key, Integer isDelete);

    /**
     * 获取相应id的参数
     *
     * @param id 参数id
     * @return 参数对象
     */
    Param findTopById(Long id);

    /**
     * 获取相应条件的参数分页列表
     *
     * @param specification 条件
     * @param pageable      分页
     * @return 参数对象分页列表
     */
    Page<Param> findAll(Specification specification, Pageable pageable);

}
