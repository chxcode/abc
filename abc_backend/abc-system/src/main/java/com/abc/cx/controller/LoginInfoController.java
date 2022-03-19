package com.abc.cx.controller;

import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.service.ILoginInfoService;
import com.abc.cx.service.impl.LoginInfoServiceImpl;
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
 * @Decription: 登录日志管理
 * @Date: 10:40 2021/11/7
 **/
@Api(tags = "登录日志管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/loginInfo")
public class LoginInfoController {

    private ILoginInfoService loginInfoService;

    @Autowired
    public void setLoginInfoServiceImpl(LoginInfoServiceImpl loginInfoService) {
        this.loginInfoService = loginInfoService;
    }

    @ApiOperation("获取登录日志分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态", dataType = "OperStatus", paramType = "query"),
            @ApiImplicitParam(name = "beginDate", value = "开始日期", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "int", paramType = "query"),
    })
    @GetMapping(value = "/pages")
    public Ret list(@RequestParam(defaultValue = "") OperateStatusEnum status,
                    @RequestParam(defaultValue = "") String beginDate,
                    @RequestParam(defaultValue = "") String endDate,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer pageSize) {
        return loginInfoService.pageLoginInfos(status, beginDate, endDate, page, pageSize);
    }

    @ApiOperation("获取登录日志详情")
    @ApiImplicitParam(name = "id", value = "部门id", required = true, dataType = "long", paramType = "path")
    @GetMapping(value = "/{id}")
    public Ret get(@PathVariable Long id) {
        return loginInfoService.getLoginInfo(id);
    }

}
