package com.abc.cx.domain.repository;

import com.abc.cx.domain.OperateLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: ChangXuan
 * @Decription: 操作日志
 * @Date: 22:29 2021/11/6
 **/
public interface OperateLogRepository extends JpaRepository<OperateLog, Long> {

    /**
     * 获取相应id的操作日志
     *
     * @param id 操作日志id
     * @return 操作日志对象
     */
    OperateLog findTopById(Long id);

    /**
     * 获取相应条件的操作日志分页列表
     *
     * @param specification 条件
     * @param pageable      分页
     * @return 操作日志对象分页列表
     */
    Page<OperateLog> findAll(Specification specification, Pageable pageable);

}
