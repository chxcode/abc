package com.abc.cx.domain.repository;

import com.abc.cx.domain.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 角色菜单
 * @Date: 22:31 2021/11/6
 **/
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {

    /**
     * 删除角色所有菜单
     *
     * @param roleId 角色id
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByRoleId(Long roleId);

    /**
     * 获取相应角色的角色菜单列表
     *
     * @param roleId 角色id
     * @return 角色菜单对象列表
     */
    List<RoleMenu> findAllByRoleId(Long roleId);

    /**
     * 获取相应菜单的角色菜单列表
     *
     * @param menuId 角色id
     * @return 角色菜单对象列表
     */
    List<RoleMenu> findAllByMenuId(Long menuId);

}
