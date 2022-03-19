package com.abc.cx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.abc.cx.constant.SysConstants;
import com.abc.cx.domain.*;
import com.abc.cx.domain.repository.RoleDeptRepository;
import com.abc.cx.domain.repository.RoleMenuRepository;
import com.abc.cx.domain.repository.RoleRepository;
import com.abc.cx.domain.repository.UserRepository;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.enums.ResultEnum;
import com.abc.cx.service.IRoleService;
import com.abc.cx.vo.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:48 2021/11/6
 **/
@Service
public class RoleServiceImpl implements IRoleService {

    private UserServiceImpl userService;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    private MenuServiceImpl menuService;

    @Autowired
    public void setMenuServiceImpl(MenuServiceImpl menuService) {
        this.menuService = menuService;
    }

    private DeptServiceImpl deptService;

    @Autowired
    public void setDeptServiceImpl(DeptServiceImpl deptService) {
        this.deptService = deptService;
    }

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private RoleMenuRepository roleMenuRepository;

    @Autowired
    public void setRoleMenuRepository(RoleMenuRepository roleMenuRepository) {
        this.roleMenuRepository = roleMenuRepository;
    }

    private RoleDeptRepository roleDeptRepository;

    @Autowired
    public void setRoleDeptRepository(RoleDeptRepository roleDeptRepository) {
        this.roleDeptRepository = roleDeptRepository;
    }

    /**
     * 获取角色列表
     *
     * @param name      名称
     * @param status    状态
     * @param key       键值
     * @param orderNum  排序
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 角色对象列表结果
     */
    @Override
    public Ret listRoles(String name, EnableStatusEnum status, String key, String orderNum, String beginDate, String endDate) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "orderNum");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        Sort sort = Sort.by(orders);
        Specification<Role> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(builder.equal(root.get("isDelete"), 0));
            if (StrUtil.isNotEmpty(name)) {
                predicateList.add(builder.isNotNull(root.get("name")));
                predicateList.add(builder.like(root.get("name"), "%" + name + "%"));
            }
            if (StrUtil.isNotEmpty(key)) {
                predicateList.add(builder.isNotNull(root.get("key")));
                predicateList.add(builder.like(root.get("key"), "%" + key + "%"));
            }
            if (StrUtil.isNotEmpty(orderNum)) {
                predicateList.add(builder.isNotNull(root.get("orderNum")));
                predicateList.add(builder.equal(root.get("orderNum"), orderNum));
            }
            if (status != null) {
                predicateList.add(builder.isNotNull(root.get("status")));
                predicateList.add(builder.equal(root.get("status"), status));
            }
            if (StrUtil.isNotEmpty(beginDate) && StrUtil.isNotEmpty(endDate)) {
                String keyBegin = beginDate + " 00:00:00";
                String keyEnd = endDate + " 23:59:59";
                predicateList.add(builder.between(root.get("createTime"), DateUtil.parse(keyBegin), DateUtil.parse(keyEnd)));
            }
            Predicate[] arrayType = new Predicate[predicateList.size()];
            return builder.and(predicateList.toArray(arrayType));
        };
        List<Role> list = roleRepository.findAll(specification, sort);
        return Ret.ok("list", list);
    }

    @Override
    public Role getRoleByKey(String key) {
        return roleRepository.findTopByKeyAndIsDelete(key, 1);
    }

    /**
     * 获取角色详细信息
     *
     * @param id 角色对象id
     * @return 角色对象详情结果
     */
    @Override
    public Ret getRole(Long id) {
        Role role = roleRepository.findTopById(id);
        if (role == null) {
            return Ret.failMsg(ResultEnum.ROLE_NOT_FOUND.getMessage());
        }
        //根据FUI返回角色菜单key列表
        List<String> menuKeyList = new ArrayList<>();
        Ret menuRet = menuService.getRoleMenuTreeKey(id);
        if (menuRet.isOk()) {
            menuKeyList = (List<String>) menuRet.get("menuKeyList");
        }
        role.setMenuKeyList(menuKeyList);
        //根据FUI返回角色部门key列表
        List<String> deptKeyList = new ArrayList<>();
        Ret deptRet = deptService.getRoleDeptTreeKey(id);
        if (deptRet.isOk()) {
            deptKeyList = (List<String>) deptRet.get("deptKeyList");
        }
        role.setDeptKeyList(deptKeyList);
        return Ret.ok("role", role);
    }

    /**
     * 新增角色
     *
     * @param role 角色对象
     * @return 新增结果
     */
    @Override
    public Ret insertRole(Role role) {
        User user = userService.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        if (isExist(role)) {
            return Ret.failMsg(ResultEnum.ROLE_EXIT.getMessage());
        }
        role.setCreateUser(user);
        roleRepository.save(role);
        if (role.getMenuList() != null && role.getMenuList().size() > 0) {
            for (Menu menu : role.getMenuList()) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(role.getId());
                roleMenu.setMenuId(menu.getId());
                roleMenuRepository.save(roleMenu);
            }
        }
        return Ret.ok("message", "增加角色成功");
    }

    /**
     * 修改角色
     *
     * @param role 角色对象
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Ret updateRole(Role role) {
        if (isExist(role)) {
            return Ret.failMsg(ResultEnum.ROLE_EXIT.getMessage());
        }
        User user = userService.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        //停用账号判断
        if (role.getStatus() != null && role.getStatus() == EnableStatusEnum.DISABLED) {
            if (role.getId() == 1) {
                return Ret.failMsg(ResultEnum.ROLE_IS_ADMIN.getMessage());
            }
            if (isUse(role)) {
                return Ret.failMsg(ResultEnum.ROLE_USER_USE.getMessage());
            }
        }
        if (role.getStatus() != null && role.getStatus() == EnableStatusEnum.ENABLED) {
            if (isExist(role)) {
                return Ret.failMsg(ResultEnum.ROLE_EXIT.getMessage());
            }
        }
        Role oldRole = roleRepository.findTopById(role.getId());
        BeanUtil.copyProperties(role, oldRole, CopyOptions.create().setIgnoreNullValue(true));
        oldRole.setUpdateUser(user);
        roleRepository.save(oldRole);
        if (role.getMenuList() != null) {
            roleMenuRepository.deleteAllByRoleId(oldRole.getId());
            for (Menu menu : role.getMenuList()) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(oldRole.getId());
                roleMenu.setMenuId(menu.getId());
                roleMenuRepository.save(roleMenu);
            }
        }
        if (role.getDeptList() != null) {
            roleDeptRepository.deleteAllByRoleId(oldRole.getId());
            for (Dept dept : role.getDeptList()) {
                RoleDept roleDept = new RoleDept();
                roleDept.setRoleId(oldRole.getId());
                roleDept.setDeptId(dept.getId());
                roleDeptRepository.save(roleDept);
            }
        }
        return Ret.ok("message", "修改角色成功");
    }

    /**
     * 删除角色
     *
     * @param id 角色对象id
     * @return 删除结果
     */
    @Override
    public Ret deleteRole(Long id) {
        User user = userService.getCurrentLoginUser();
        if (user == null) {
            return Ret.failMsg(ResultEnum.USER_NOT_LOGIN.getMessage());
        }
        Role role = roleRepository.findTopById(id);
        if (role == null) {
            return Ret.failMsg(ResultEnum.ROLE_NOT_FOUND.getMessage());
        }
        if (id.equals(SysConstants.DEFAULT_ID)) {
            return Ret.failMsg(ResultEnum.ROLE_IS_ADMIN.getMessage());
        }
        if (isUse(role)) {
            return Ret.failMsg(ResultEnum.ROLE_USER_USE.getMessage());
        }
        roleMenuRepository.deleteAllByRoleId(id);
        roleDeptRepository.deleteAllByRoleId(id);
        role.setIsDelete(1);
        role.setUpdateUser(user);
        roleRepository.save(role);
        return Ret.ok("message", "删除角色成功");
    }

    /**
     * 批量删除角色
     *
     * @param ids 角色id列表
     * @return 批量删除结果
     */
    @Override
    public Ret deleteRoles(List<Long> ids) {
        int successCount = 0;
        int failCount = 0;
        List<Role> list = roleRepository.findAllByIdIn(ids);
        for (Role role : list) {
            Ret result = deleteRole(role.getId());
            if (result.isOk()){
                successCount++;
            }else {
                failCount++;
            }
        }
        return Ret.ok("message", "批量删除成功"+successCount+"条，失败"+failCount+"条");
    }

    /**
     * 判断角色名是否已存在
     *
     * @param role 角色对象
     * @return 结果 true 是 false 否
     */
    private boolean isExist(Role role) {
        Role existRole = roleRepository.findTopByNameAndIsDeleteAndStatus(role.getName(), 0, EnableStatusEnum.ENABLED);
        if (existRole == null) {
            return false;
        } else {
            return role.getId() == null || !role.getId().equals(existRole.getId());
        }
    }

    /**
     * 判断角色是否已在使用
     *
     * @param role 角色对象
     * @return 结果 true 是 false 否
     */
    private boolean isUse(Role role) {
        List<User> userList = userRepository.findAllByRoleAndIsDelete(role, 0);
        return !userList.isEmpty();
    }

}
