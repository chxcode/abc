package com.abc.cx.domain.repository;

import com.abc.cx.domain.Menu;
import com.abc.cx.enums.VisibleTypeEnum;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 菜单
 * @Date: 22:29 2021/11/6
 **/
public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * 获取相应id的菜单
     *
     * @param id 菜单id
     * @return 菜单对象
     */
    Menu findTopById(Long id);

    /**
     * 获取相应条件的菜单
     *
     * @param name       菜单名称
     * @param parentMenu 父菜单
     * @param isDelete   是否删除
     * @param visible    是否可见
     * @return 菜单对象
     */
    Menu findTopByNameAndParentMenuAndIsDeleteAndVisible(String name, Menu parentMenu, Integer isDelete, VisibleTypeEnum visible);

    /**
     * 获取相应条件的菜单
     *
     * @param url      url
     * @param isDelete 是否删除
     * @param visible  是否可见
     * @return 菜单对象
     */
    Menu findTopByUrlAndIsDeleteAndVisible(String url, Integer isDelete, VisibleTypeEnum visible);

    /**
     * 获取相应条件的菜单列表
     *
     * @param specification 条件
     * @param sort          排序
     * @return 菜单对象列表
     */
    List<Menu> findAll(Specification specification, Sort sort);

    /**
     * 获取相应条件的菜单列表
     *
     * @param isDelete   是否删除
     * @param parentMenu 菜单名称
     * @return 菜单对象列表
     */
    List<Menu> findAllByIsDeleteAndParentMenuOrderByOrderNum(Integer isDelete, Menu parentMenu);

    /**
     * 获取相应条件的菜单列表
     *
     * @param isDelete   是否删除
     * @param visible    是否可见
     * @param parentMenu 菜单名称
     * @return 菜单对象列表
     */
    List<Menu> findAllByIsDeleteAndVisibleAndParentMenuOrderByOrderNum(Integer isDelete, VisibleTypeEnum visible, Menu parentMenu);

    /**
     * 获取相应角色的菜单列表
     *
     * @param roleId 角色id
     * @return 菜单对象列表
     */
    @Query(value = "select m.* from sys_menu m, sys_role_menu l  where m.id = l.menu_id and l.role_id = :roleId and m.visible = 0 and is_delete = 0 order by order_num", nativeQuery = true)
    List<Menu> findAllByRole(@Param("roleId") Long roleId);

    /**
     * 获取相应角色的主菜单列表
     *
     * @param roleId 角色id
     * @return 菜单对象列表
     */
    @Query(value = "select m.* from sys_menu m, sys_role_menu l  where m.id = l.menu_id and m.type = 'MAIN' and l.role_id = :roleId and m.visible = 0 and is_delete = 0 order by order_num", nativeQuery = true)
    List<Menu> findRoleMainMenu(@Param("roleId") Long roleId);

}
