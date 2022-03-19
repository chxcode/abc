package com.abc.cx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.abc.cx.constant.SysConstants;
import com.abc.cx.domain.Menu;
import com.abc.cx.domain.RoleMenu;
import com.abc.cx.domain.User;
import com.abc.cx.domain.repository.MenuRepository;
import com.abc.cx.domain.repository.RoleMenuRepository;
import com.abc.cx.domain.repository.RoleRepository;
import com.abc.cx.enums.MenuTypeEnum;
import com.abc.cx.enums.ResultEnum;
import com.abc.cx.enums.VisibleTypeEnum;
import com.abc.cx.service.IMenuService;
import com.abc.cx.vo.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:48 2021/11/6
 **/
@Service
public class MenuServiceImpl implements IMenuService {

    private UserServiceImpl userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    private MenuRepository menuRepository;

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    private RoleMenuRepository roleMenuRepository;

    @Autowired
    public void setRoleMenuRepository(RoleMenuRepository roleMenuRepository) {
        this.roleMenuRepository = roleMenuRepository;
    }

    /**
     * 获取角色主菜单列表
     *
     * @return
     */
    @Override
    public Ret getMainMenuList() {
        User user = userService.getCurrentLoginUser();
        List<Menu> list;
        list = menuRepository.findRoleMainMenu(user.getRole().getId());
        return Ret.ok("list", list);
    }

    /**
     * 获取角色侧栏菜单列表
     *
     * @param id
     * @return
     */
    @Override
    public Ret getSideMenuListByMain(Long id) {
        User user = userService.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        List<Menu> list = menuRepository.findAllByRole(user.getRole().getId());
        List<Menu> menuList = getChildList(id, list);
        return Ret.ok("menuList", menuList);
    }

    /**
     * 递归查找子菜单
     *
     * @param id
     * @param menuList
     * @return
     */
    private List<Menu> getChildList(Long id, List<Menu> menuList) {
        // 子菜单
        List<Menu> childList = new ArrayList<>();
        for (Menu menu : menuList) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (menu.getParentMenu() != null) {
                if (menu.getParentMenu().getId().equals(id)) {
                    childList.add(menu);
                }
            }
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        // 把子菜单的子菜单再循环一遍
        List<Menu> childMenuList = new ArrayList<>();
        for (Menu menu : childList) {
            // 递归
            List<Menu> nextChildList = getChildList(menu.getId(), menuList);
            if (nextChildList != null) {
                childMenuList.addAll(nextChildList);
            }
        }
        childList.addAll(childMenuList);
        return childList;
    }

    /**
     * 获取角色菜单树值
     *
     * @param roleId
     * @return
     */
    @Override
    public Ret getRoleMenuTreeKey(Long roleId) {
        User user = userService.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        List<RoleMenu> roleMenuList = roleMenuRepository.findAllByRoleId(roleId);
        List<Menu> list = new ArrayList<>();
        List<String> menuKeyList = new ArrayList<>();
        for (RoleMenu roleMenu : roleMenuList) {
            Menu menu = menuRepository.findTopById(roleMenu.getMenuId());
            list.add(menu);
        }
        for (Menu menu : list) {
            List<Menu> children = menuRepository.findAllByIsDeleteAndVisibleAndParentMenuOrderByOrderNum(0, VisibleTypeEnum.VISIBLE, menu);
            if (children.size() == 0) {
                menuKeyList.add(menu.getId().toString());
            }
        }
        return Ret.ok("menuKeyList", menuKeyList);
    }

    /**
     * 获取菜单列表
     *
     * @param name    名称
     * @param type    类型
     * @param visible 是否可见
     * @return 菜单对象列表结果
     */
    @Override
    public Ret listMenus(String name, String type, VisibleTypeEnum visible) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "orderNum");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        Sort sort = Sort.by(orders);
        Specification<Menu> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("isDelete"), 0));
            if (StrUtil.isNotEmpty(name)) {
                predicateList.add(builder.isNotNull(root.get("name")));
                predicateList.add(builder.like(root.get("name"), "%" + name + "%"));
            }
            if (visible != null) {
                predicateList.add(builder.isNotNull(root.get("visible")));
                predicateList.add(builder.equal(root.get("visible"), visible));
            }
            if (StrUtil.isNotEmpty(type)) {
                predicateList.add(builder.isNotNull(root.get("type")));
                predicateList.add(builder.equal(root.get("type"), type));
            }
            Predicate[] arrayType = new Predicate[predicateList.size()];
            return builder.and(predicateList.toArray(arrayType));
        };
        List<Menu> list = menuRepository.findAll(specification, sort);
        return Ret.ok("list", list);
    }

    /**
     * 获取页面上的按钮列表
     *
     * @param parentId 父菜单id
     * @return
     */
    @Override
    public Ret getButtonListByPage(Long parentId) {
        Menu parentMenu = menuRepository.findTopById(parentId);
        List<Menu> list = menuRepository.findAllByIsDeleteAndVisibleAndParentMenuOrderByOrderNum(0, VisibleTypeEnum.VISIBLE, parentMenu);
        return Ret.ok("list", list);
    }

    /**
     * 获取菜单详细信息
     *
     * @param id 菜单对象id
     * @return 菜单对象详情结果
     */
    @Override
    public Ret getMenu(Long id) {
        Menu menu = menuRepository.findTopById(id);
        return Ret.ok("menu", menu);
    }

    /**
     * 增加菜单
     *
     * @param menu 菜单对象
     * @return 增加结果
     */
    @Override
    public Ret insertMenu(Menu menu) {
        User user = userService.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        // 取消同名菜单与 URL 限制
//        if (isNameExist(menu)) {
//            return Ret.failMsg(ResultEnum.MENU_NAME_EXIT.getMessage());
//        }
//        if (isUrlExist(menu)) {
//            return Ret.failMsg(ResultEnum.MENU_URL_EXIT.getMessage());
//        }
        if (menu.getParentMenu() != null && menu.getVisible() == VisibleTypeEnum.VISIBLE) {
            if (isParentHide(menu)) {
                return Ret.failMsg(ResultEnum.MENU_PARENT_HIDE.getMessage());
            }
        }
        Ret ret = isRightParent(menu);
        if (ret.isFail()) {
            return Ret.failMsg(ret.get("msg").toString());
        }
        menu.setCreateUser(user);
        menuRepository.save(menu);
        return Ret.ok("message", "增加菜单成功");
    }

    /**
     * 修改菜单
     *
     * @param menu 菜单对象
     * @return 修改结果
     */
    @Override
    public Ret updateMenu(Menu menu) {
        User user = userService.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        if (isNameExist(menu)) {
            return Ret.failMsg(ResultEnum.MENU_NAME_EXIT.getMessage());
        }
        if (isUrlExist(menu)) {
            return Ret.failMsg(ResultEnum.MENU_URL_EXIT.getMessage());
        }
        if (menu.getParentMenu() != null) {
            if (isChild(menu, menu.getParentMenu())) {
                return Ret.failMsg(ResultEnum.PARENT_IS_CHILD.getMessage());
            }
        }
        Ret ret = isRightParent(menu);
        if (ret.isFail()) {
            return Ret.failMsg(ret.get("msg").toString());
        }
        if (menu.getVisible() != null && menu.getVisible() == VisibleTypeEnum.HIDE) {
            if (isUse(menu)) {
                return Ret.failMsg(ResultEnum.MENU_ROLE_USE.getMessage());
            }
            if (isChildVisible(menu)) {
                return Ret.failMsg(ResultEnum.MENU_CHILD_VISIBLE.getMessage());
            }
        }
        if (menu.getParentMenu() != null && menu.getVisible() == VisibleTypeEnum.VISIBLE) {
            if (isParentHide(menu)) {
                return Ret.failMsg(ResultEnum.MENU_PARENT_HIDE.getMessage());
            }
        }
        Menu oldMenu = menuRepository.findTopById(menu.getId());
        BeanUtil.copyProperties(menu, oldMenu, CopyOptions.create().setIgnoreNullValue(true));
        if (menu.getParentMenu() == null) {
            oldMenu.setParentMenu(null);
        }
        oldMenu.setUpdateUser(user);
        menuRepository.save(oldMenu);
        return Ret.ok("message", "修改菜单成功");
    }

    /**
     * 删除菜单
     *
     * @param id 菜单对象id
     * @return 删除结果
     */
    @Override
    public Ret deleteMenu(Long id) {
        User user = userService.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        Menu menu = menuRepository.findTopById(id);
        if (menu == null) {
            return Ret.failMsg(ResultEnum.MENU_NOT_FOUND.getMessage());
        }
        if (isChildNotDelete(menu)) {
            return Ret.failMsg(ResultEnum.CHILD_NOT_DELETE.getMessage());
        }
        if (isUse(menu)) {
            return Ret.failMsg(ResultEnum.MENU_ROLE_USE.getMessage());
        }
        menu.setIsDelete(1);
        menu.setUpdateUser(user);
        menuRepository.save(menu);
        return Ret.ok("message", "删除菜单成功");
    }

    /**
     * 同一父级下是否有同名菜单
     *
     * @param menu 菜单对象
     * @return 结果 true 是 false 否
     */
    private boolean isNameExist(Menu menu) {
        Menu existMenu = menuRepository.findTopByNameAndParentMenuAndIsDeleteAndVisible(menu.getName(), menu.getParentMenu(), 0, VisibleTypeEnum.VISIBLE);
        if (existMenu == null) {
            return false;
        } else {
            return menu.getId() == null || !menu.getId().equals(existMenu.getId());
        }
    }

    /**
     * 菜单Url不为#时是否相同
     *
     * @param menu 菜单对象
     * @return 结果 true 是 false 否
     */
    private boolean isUrlExist(Menu menu) {
        if (StrUtil.isEmpty(menu.getUrl()) || SysConstants.EMPTY_URL.equals(menu.getUrl())) {
            return false;
        }
        Menu existMenu = menuRepository.findTopByUrlAndIsDeleteAndVisible(menu.getUrl(), 0, VisibleTypeEnum.VISIBLE);
        if (existMenu == null) {
            return false;
        } else {
            return menu.getId() == null || !menu.getId().equals(existMenu.getId());
        }
    }


    /**
     * 修改菜单时不能挂载到子菜单下
     *
     * @param menu 菜单对象
     * @return 结果 true 是 false 否
     */
    private boolean isChild(Menu menu, Menu parentMenu) {
        Menu curParentMenu = menuRepository.findTopById(parentMenu.getId());
        if (menu.getId().equals(parentMenu.getId())) {
            return true;
        }
        if (curParentMenu.getParentMenu() == null) {
            return false;
        }
        return isChild(menu, curParentMenu.getParentMenu());
    }

    /**
     * 菜单挂载判断，规则如下：
     * 目录（主菜单）只能挂载在目录下，或为根目录
     * 菜单（侧栏菜单）只能挂载在目录或菜单下
     * 按钮只能挂载在菜单下
     *
     * @param menu 菜单对象
     * @return 结果 true 是 false 否
     */
    private Ret isRightParent(Menu menu) {
        Menu parentMenu = null;
        if (menu.getParentMenu() != null) {
            parentMenu = menuRepository.findTopById(menu.getParentMenu().getId());
        }
        if (MenuTypeEnum.MAIN.toString().equals(menu.getType())) {
            if (parentMenu != null && !MenuTypeEnum.MAIN.toString().equals(parentMenu.getType())) {
                return Ret.failMsg("目录只能为根节点");
            }
        } else if (MenuTypeEnum.CHILD.toString().equals(menu.getType())) {
            if (parentMenu == null) {
                return Ret.failMsg("菜单不能为根节点");
            } else if (MenuTypeEnum.BUTTON.toString().equals(parentMenu.getType())) {
                return Ret.failMsg("菜单不能挂载在按钮下");
            }
        } else {
            if (parentMenu == null || MenuTypeEnum.BUTTON.toString().equals(parentMenu.getType())) {
                return Ret.failMsg("按钮只能挂载在菜单下");
            }
        }
        return Ret.ok();
    }

    /**
     * 判断菜单是否已在使用
     *
     * @param menu 菜单对象
     * @return 结果 true 是 false 否
     */
    private boolean isUse(Menu menu) {
        List<RoleMenu> roleMenuList = roleMenuRepository.findAllByMenuId(menu.getId());
        return !roleMenuList.isEmpty();
    }

    /**
     * 是否存在未删除的子菜单
     *
     * @param menu 菜单对象
     * @return 结果 true 是 false 否
     */
    private boolean isChildNotDelete(Menu menu) {
        List<Menu> list = menuRepository.findAllByIsDeleteAndParentMenuOrderByOrderNum(0, menu);
        return !list.isEmpty();
    }

    /**
     * 是否存在未删除且显示的子菜单
     *
     * @param menu 菜单对象
     * @return 结果 true 是 false 否
     */
    private boolean isChildVisible(Menu menu) {
        List<Menu> list = menuRepository.findAllByIsDeleteAndVisibleAndParentMenuOrderByOrderNum(0, VisibleTypeEnum.VISIBLE, menu);
        return !list.isEmpty();
    }

    /**
     * 父菜单是否隐藏
     *
     * @param menu 菜单对象
     * @return 结果 true 是 false 否
     */
    private boolean isParentHide(Menu menu) {
        Menu parentMenu = menuRepository.findTopById(menu.getParentMenu().getId());
        return parentMenu.getVisible() == VisibleTypeEnum.HIDE;
    }

    /**
     * 初始化时插入菜单
     *
     * @param name       名称
     * @param type       类型
     * @param url        url
     * @param orderNum   排序
     * @param parentMenu 父菜单
     * @param remark     备注
     * @return 菜单对象
     */
    @Override
    public Menu insertMenu(String name, String type, String url, Integer orderNum, Menu parentMenu, String remark) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setType(type);
        menu.setUrl(url);
        menu.setOrderNum(orderNum);
        menu.setParentMenu(parentMenu);
        menu.setVisible(VisibleTypeEnum.VISIBLE);
        menu.setRemark(remark);
        return menuRepository.save(menu);
    }

}
