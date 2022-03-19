package com.abc.cx.domain.repository;

import com.abc.cx.domain.UserOnline;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 在线用户
 * @Date: 22:31 2021/11/6
 **/
public interface UserOnlineRepository extends JpaRepository<UserOnline, Long> {

    /**
     * 获取相应id的在线用户
     *
     * @param id 在线用户id
     * @return 在线用户对象
     */
    UserOnline findTopById(Long id);

    /**
     * 获取相应username的在线用户
     *
     * @param username username
     * @return 在线用户对象
     */
    UserOnline findTopByUsername(String username);

    /**
     * 获取相应sessionId的在线用户
     *
     * @param sessionId sessionId
     * @return 在线用户对象
     */
    UserOnline findTopBySessionId(String sessionId);

    /**
     * 获取相应条件的在线用户列表
     *
     * @param specification 条件
     * @param sort          排序
     * @return 在线用户对象列表
     */
    List<UserOnline> findAll(Specification specification, Sort sort);

}
