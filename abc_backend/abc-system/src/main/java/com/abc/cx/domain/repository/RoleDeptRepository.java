package com.abc.cx.domain.repository;

import com.abc.cx.domain.RoleDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 角色部门
 * @Date: 22:30 2021/11/6
 **/
public interface RoleDeptRepository extends JpaRepository<RoleDept, Long> {

    /**
     * 删除角色所有部门
     *
     * @param roleId 角色id
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByRoleId(Long roleId);

    /**
     * 获取相应角色的角色部门列表
     *
     * @param roleId 角色id
     * @return 角色部门对象列表
     */
    List<RoleDept> findAllByRoleId(Long roleId);

    /**
     * 获取相应部门的角色部门列表
     *
     * @param deptId 角色id
     * @return 角色部门对象列表
     */
    List<RoleDept> findAllByDeptId(Long deptId);

}
