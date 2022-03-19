package com.abc.cx.service;

import com.abc.cx.domain.Menu;
import com.abc.cx.enums.VisibleTypeEnum;
import com.abc.cx.vo.Ret;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:50 2021/11/6
 **/
public interface IMenuService {

    /**
     * 获取角色主菜单列表
     *
     * @return ret
     */
    Ret getMainMenuList();

    /**
     * 获取角色侧栏菜单列表
     *
     * @param id
     * @return
     */
    Ret getSideMenuListByMain(Long id);

    /**
     * 获取角色菜单树值
     *
     * @param roleId
     * @return
     */
    Ret getRoleMenuTreeKey(Long roleId);

    /**
     * 获取菜单列表
     *
     * @param name    名称
     * @param type    类型
     * @param visible 是否可见
     * @return 菜单对象列表结果
     */
    Ret listMenus(String name, String type, VisibleTypeEnum visible);

    /**
     * 获取页面上的按钮列表
     *
     * @param parentId 父菜单id
     * @return
     */
    Ret getButtonListByPage(Long parentId);

    /**
     * 获取菜单详细信息
     *
     * @param id 菜单对象id
     * @return 菜单对象详情结果
     */
    Ret getMenu(Long id);

    /**
     * 新增菜单
     *
     * @param menu 菜单对象
     * @return 增加结果
     */
    Ret insertMenu(Menu menu);

    /**
     * 修改菜单
     *
     * @param menu 菜单对象
     * @return 修改结果
     */
    Ret updateMenu(Menu menu);

    /**
     * 删除菜单
     *
     * @param id 菜单对象id
     * @return 删除结果
     */
    Ret deleteMenu(Long id);

    /**
     * 初始化时插入菜单
     *
     * @param name       名称
     * @param type       类型
     * @param url        url
     * @param orderNum   排序
     * @param parentMenu 父菜单
     * @param remark     备注
     * @return 增加结果
     */
    Menu insertMenu(String name, String type, String url, Integer orderNum, Menu parentMenu, String remark);

}
