package com.abc.cx.controller;

import com.abc.cx.domain.User;
import com.abc.cx.service.IMenuService;
import com.abc.cx.service.IUserService;
import com.abc.cx.service.impl.MenuServiceImpl;
import com.abc.cx.service.impl.UserServiceImpl;
import com.abc.cx.vo.Ret;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 10:51 2021/11/7
 **/
@Api(tags = "NoAuth")
@Slf4j
@RestController
@RequestMapping(value = "/noauth/api")
public class NoAuthController {

    private IMenuService menuService;
    private IUserService userService;

    @Autowired
    public void setMenuServiceImpl(MenuServiceImpl menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }


    @ApiOperation("获取主菜单列表")
    @GetMapping(value = "/menu/main")
    public Ret listMain() {
        return menuService.getMainMenuList();
    }

    @ApiOperation("获取当前用户详情")
    @GetMapping(value = "/user/get")
    public Ret getCurrentUserDetail() {
        User user = userService.getCurrentLoginUser();
        return Ret.ok("user", user);
    }
}
