package com.abc.cx.controller;

import com.abc.cx.annotation.Log;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.service.IUserOnlineService;
import com.abc.cx.service.impl.UserOnlineServiceImpl;
import com.abc.cx.vo.Ret;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: ChangXuan
 * @Decription: 在线用户管理
 * @Date: 11:35 2021/11/7
 **/
@Api(tags = "在线用户管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/online")
public class UserOnlineController {

    private IUserOnlineService userOnlineService;

    @Autowired
    public void setUserOnlineServiceImpl(UserOnlineServiceImpl userOnlineService) {
        this.userOnlineService = userOnlineService;
    }

    @ApiOperation("获取在线用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登录名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "loginLocation", value = "登录地址", dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "/list")
    public Ret list(@RequestParam(defaultValue = "") String username,
                    @RequestParam(defaultValue = "") String location) {
        return userOnlineService.listUserOnline(username, location);
    }

    @Log(title = "在线用户管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("删除在线用户")
    @ApiImplicitParam(name = "userId", value = "在线用户Id", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/{userId}")
    public Ret delete(@PathVariable Long userId) {
        return userOnlineService.deleteUserOnline(userId);
    }

}
