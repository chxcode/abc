package com.abc.cx.domain.repository;

import com.abc.cx.domain.Role;
import com.abc.cx.domain.User;
import com.abc.cx.enums.EnableStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 用户
 * @Date: 22:31 2021/11/6
 **/
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 获取相应id的用户
     *
     * @param id 用户id
     * @return 用户对象
     */
    User findTopById(Long id);

    /**
     * 判断邮箱是否已存在
     * @param email 邮箱
     * @param isDelete 是否删除
     * @return Boolean
     */
    Boolean existsByEmailAndIsDelete(String email, Integer isDelete);

    /**
     * 通过邮箱查询用户
     * @param email 邮箱
     * @param isDelete 是否删除
     * @return User
     */
    User findByEmailAndIsDelete(String email, Integer isDelete);

    /**
     * 获取相应用户名的用户
     *
     * @param username 用户名
     * @param status   用户状态
     * @param isDelete 是否删除
     * @return 用户对象
     */
    User findByUsernameAndStatusAndIsDelete(String username, EnableStatusEnum status, Integer isDelete);

    /**
     * 获取相应用户名的用户
     *
     * @param username 用户名
     * @param isDelete 是否删除
     * @return 用户对象
     */
    User findByUsernameAndIsDelete(String username, Integer isDelete);

    /**
     * 获取相应条件的用户分页列表
     *
     * @param specification 条件
     * @param pageable      分页
     * @return 用户对象分页列表
     */
    Page<User> findAll(Specification specification, Pageable pageable);

    /**
     * 获取相应条件的用户列表
     *
     * @param specification 条件
     * @param sort          排序
     * @return 用户对象列表
     */
    List<User> findAll(Specification specification, Sort sort);

    /**
     * 获取相应角色的用户列表
     *
     * @param role     角色
     * @param isDelete 是否删除
     * @return 用户对象列表
     */
    List<User> findAllByRoleAndIsDelete(Role role, Integer isDelete);

    /**
     * 获取相应id的用户列表
     *
     * @param ids id列表
     * @return 用户对象列表
     */
    List<User> findAllByIdIn(List<Long> ids);

}
