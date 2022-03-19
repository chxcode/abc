package com.abc.cx.domain.repository;

import com.abc.cx.domain.LoginInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: ChangXuan
 * @Decription: 登录日志
 * @Date: 22:29 2021/11/6
 **/
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {

    /**
     * 获取相应id的登录日志
     *
     * @param id 登录日志id
     * @return 登录日志对象
     */
    LoginInfo findTopById(Long id);

    /**
     * 获取相应条件的登录日志分页列表
     *
     * @param specification 条件
     * @param pageable      分页
     * @return 登录日志对象分页列表
     */
    Page<LoginInfo> findAll(Specification specification, Pageable pageable);

}
