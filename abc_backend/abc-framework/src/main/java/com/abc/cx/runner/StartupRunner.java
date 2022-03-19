package com.abc.cx.runner;

import com.abc.cx.domain.*;
import com.abc.cx.domain.repository.*;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.enums.GenderTypeEnum;
import com.abc.cx.enums.MenuTypeEnum;
import com.abc.cx.service.impl.MenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 初始化数据库
 * @Date: 10:25 2021/5/8
 **/
@Configuration
@Order(1)
public class StartupRunner implements CommandLineRunner {

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

    private DeptRepository deptRepository;

    @Autowired
    public void setDeptRepository(DeptRepository deptRepository) {
        this.deptRepository = deptRepository;
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

    private MenuRepository menuRepository;

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    private MenuServiceImpl menuService;

    @Autowired
    public void setMenuServiceImpl(MenuServiceImpl menuService) {
        this.menuService = menuService;
    }

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        addDefaultDept();
        addDefaultRole();
        addDefaultUser();
        addDefaultMenus();
        addDefaultRoleMenus();
        addDefaultRoleDept();
    }

    private void addDefaultDept() {
        if (deptRepository.count() == 0) {
            Dept dept = Dept.builder()
                    .name("总公司")
                    .leader("常总")
                    .phone("")
                    .email("")
                    .status(EnableStatusEnum.ENABLED)
                    .orderNum(1)
                    .build();
            deptRepository.save(dept);
        }
    }

    private void addDefaultRole() {
        if (roleRepository.count() == 0) {
            Role admin = Role.builder()
                    .name("管理员")
                    .key("admin")
                    .status(EnableStatusEnum.ENABLED)
                    .build();
            roleRepository.save(admin);
            Role common = Role.builder()
                    .name("普通用户")
                    .key("common")
                    .status(EnableStatusEnum.ENABLED)
                    .build();
            roleRepository.save(common);
        }
    }

    private void addDefaultUser() {
        if (userRepository.count() == 0) {
            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("123456"))
                    .status(EnableStatusEnum.ENABLED)
                    .realName("管理员")
                    .gender(GenderTypeEnum.MAN)
                    .telephone("15762217675")
                    .email("chang-xuan@qq.com")
                    .isDelete(0)
                    .dept(deptRepository.findTopById(1L))
                    .role(roleRepository.findTopById(1L))
                    .build();
            userRepository.save(user);
        }
    }

    private void addDefaultMenus() {
        if (menuRepository.count() == 0) {
            Menu systemManageMenu = menuService.insertMenu("系统管理", MenuTypeEnum.MAIN.toString(), "/app/system-management", 1, null, "系统管理主菜单");
            menuService.insertMenu("用户管理", MenuTypeEnum.CHILD.toString(), "/app/system-management/user", 1, systemManageMenu, "用户管理菜单");
            menuService.insertMenu("角色管理", MenuTypeEnum.CHILD.toString(), "/app/system-management/role", 2, systemManageMenu, "角色管理菜单");
            menuService.insertMenu("菜单管理", MenuTypeEnum.CHILD.toString(), "/app/system-management/menu", 3, systemManageMenu, "菜单管理菜单");
            menuService.insertMenu("部门管理", MenuTypeEnum.CHILD.toString(), "/app/system-management/department", 4, systemManageMenu, "部门管理菜单");
            menuService.insertMenu("岗位管理", MenuTypeEnum.CHILD.toString(), "/app/system-management/post", 5, systemManageMenu, "岗位管理菜单");
            menuService.insertMenu("字典管理", MenuTypeEnum.CHILD.toString(), "/system/dict", 6, systemManageMenu, "字典管理菜单");
            menuService.insertMenu("参数管理", MenuTypeEnum.CHILD.toString(), "/system/params", 7, systemManageMenu, "参数管理菜单");
            menuService.insertMenu("通知公告", MenuTypeEnum.CHILD.toString(), "/system/notice", 8, systemManageMenu, "通知公告菜单");
            Menu logManageMenu = menuService.insertMenu("日志管理", MenuTypeEnum.CHILD.toString(), "#", 9, systemManageMenu, "日志管理菜单");
            menuService.insertMenu("操作日志", MenuTypeEnum.CHILD.toString(), "/app/system-management/log/oper-log", 1, logManageMenu, "操作日志菜单");
            menuService.insertMenu("登录日志", MenuTypeEnum.CHILD.toString(), "/app/system-management/log/login-log", 2, logManageMenu, "登录日志菜单");
        }
    }

    private void addDefaultRoleMenus() {
        if (roleMenuRepository.count() == 0) {
            List<Menu> menuList = menuRepository.findAll();
            for (Menu menu : menuList) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(1L);
                roleMenu.setMenuId(menu.getId());
                roleMenuRepository.save(roleMenu);
            }
        }
    }

    private void addDefaultRoleDept() {
        if (roleDeptRepository.count() == 0) {
            RoleDept roleDept = new RoleDept();
            roleDept.setDeptId(1L);
            roleDept.setRoleId(1L);
            roleDeptRepository.save(roleDept);
        }
    }

}
