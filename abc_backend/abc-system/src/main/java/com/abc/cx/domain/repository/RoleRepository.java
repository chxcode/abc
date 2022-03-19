package com.abc.cx.domain.repository;

import com.abc.cx.domain.Role;
import com.abc.cx.enums.EnableStatusEnum;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 角色
 * @Date: 22:31 2021/11/6
 **/
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 获取相应id的角色
     *
     * @param id 角色id
     * @return 角色对象
     */
    Role findTopById(Long id);

    /**
     * 获取相应key的角色
     *
     * @param key      键值
     * @param isDelete 是否删除
     * @return 角色对象
     */
    Role findTopByKeyAndIsDelete(String key, Integer isDelete);

    /**
     * 获取相应name的角色
     *
     * @param name     角色名称
     * @param isDelete 是否删除
     * @param status   角色状态
     * @return 角色对象
     */
    Role findTopByNameAndIsDeleteAndStatus(String name, Integer isDelete, EnableStatusEnum status);

    /**
     * 获取相应条件的角色列表
     *
     * @param specification 条件
     * @param sort          排序
     * @return 角色对象列表
     */
    List<Role> findAll(Specification specification, Sort sort);

    /**
     * 获取相应id的角色
     *
     * @param ids 角色id列表
     * @return 角色对象
     */
    List<Role> findAllByIdIn(List<Long> ids);

}
