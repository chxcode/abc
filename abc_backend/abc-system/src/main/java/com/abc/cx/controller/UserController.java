package com.abc.cx.controller;

import cn.hutool.core.util.StrUtil;
import com.abc.cx.annotation.Log;
import com.abc.cx.domain.User;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.enums.GenderTypeEnum;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.service.IUserService;
import com.abc.cx.service.impl.UserServiceImpl;
import com.abc.cx.vo.Ret;
import com.abc.cx.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @Author: ChangXuan
 * @Decription: 用户管理
 * @Date: 11:30 2021/11/7
 **/
@Api(tags = "用户管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/user")
public class UserController {

    private IUserService userService;

    @Autowired
    public void setUserServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @ApiOperation("获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "realName", value = "用户姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "telephone", value = "手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "性别", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "用户状态", dataType = "UserStatus", paramType = "query"),
            @ApiImplicitParam(name = "beginDate", value = "开始日期", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deptId", value = "部门", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "Integer", paramType = "query"),
    })
    @GetMapping(value = "/pages")
    public Ret page(@RequestParam(defaultValue = "") String username,
                    @RequestParam(defaultValue = "") String realName,
                    @RequestParam(defaultValue = "") String telephone,
                    @RequestParam(defaultValue = "") GenderTypeEnum gender,
                    @RequestParam(defaultValue = "") EnableStatusEnum status,
                    @RequestParam(defaultValue = "") String beginDate,
                    @RequestParam(defaultValue = "") String endDate,
                    @RequestParam(defaultValue = "") Long deptId,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.pageUsers(username, realName, telephone, gender, status, beginDate, endDate, deptId, page, pageSize);
    }

    @ApiOperation("获取用户详情")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/{id:\\d+}")
    public Ret get(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @ApiOperation("修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/updatePassword")
    public Ret editPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        Ret ret;
        if (oldPassword == null || newPassword == null) {
            ret = Ret.failMsg("缺少参数");
        } else {
            ret = userService.updatePassword(oldPassword, newPassword);
        }
        return ret;
    }

    @Log(title = "用户管理", operateType = OperateTypeEnum.ADD)
    @ApiOperation("增加用户")
    @ApiImplicitParam(name = "userVO", value = "用户VO对象", required = true, dataType = "User", paramType = "body")
    @PostMapping
    public Ret add(@Validated({UserVO.Valid.class}) @RequestBody UserVO userVO, BindingResult errors) {
        if (errors.hasErrors()) {
            return Ret.failMsg(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
        } else {
            return userService.insertUser(userVO);
        }
    }

    @Log(title = "用户管理", operateType = OperateTypeEnum.UPDATE)
    @ApiOperation("修改用户")
    @ApiImplicitParam(name = "userVO", value = "用户VO对象", required = true, dataType = "User", paramType = "body")
    @PutMapping
    public Ret update(@Validated({User.Valid.class}) @RequestBody UserVO userVO, BindingResult errors) {
        if (errors.hasErrors()) {
            return Ret.failMsg(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
        } else {
            return userService.updateUser(userVO);
        }
    }

    @Log(title = "用户管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("删除用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public Ret delete(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @Log(title = "用户管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("批量删除用户")
    @ApiImplicitParam(name = "ids", value = "数据id集合", required = true, dataType = "List", paramType = "query")
    @DeleteMapping("/batch")
    public Ret deleteBatch(@RequestParam List<Long> ids) {
        return userService.deleteUsers(ids);
    }

    @ApiOperation("获取菜单")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/{id}/menus")
    public Ret getMenu(@PathVariable Long id) {
        return userService.getMenuList(id);
    }

    @ApiOperation("获取当前用户详情")
    @GetMapping(value = "/get")
    public Ret getCurrentUserDetail() {
        User user = userService.getCurrentLoginUser();
        Ret ret = Ret.ok("user", user);
        return ret;
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Ret register(@Validated(value = UserVO.Register.class) @RequestBody UserVO userVO, BindingResult errors, HttpServletRequest httpServletRequest) {
        if (errors.hasErrors()) {
            return Ret.failMsg(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
        } else {
            return userService.register(userVO, httpServletRequest);
        }
    }

    @ApiOperation(value = "用户是否存在", notes = "核验项：邮箱、用户名")
    @GetMapping("/check")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "账号", dataType = "String", paramType = "query"),
    })
    public Ret check(@RequestParam(defaultValue = "") String email,
                     @RequestParam(defaultValue = "") String username){
        if (StrUtil.isNotBlank(email)) {
            return userService.emailIsExit(email);
        } else if (StrUtil.isNotBlank(username)){
            return userService.userNameIsExit(username);
        } else {
            return Ret.failMsg("参数错误");
        }
    }

    @ApiOperation("用户激活")
    @PutMapping("/verify/{key}")
    public Ret verify(@PathVariable String key) {
        return userService.accountVerify(key);
    }

    @ApiOperation("找回密码")
    @GetMapping("/retrieve")
    @ApiImplicitParam(name = "email", value = "邮箱", dataType = "String", paramType = "query")
    public Ret retrieve(@RequestParam(defaultValue = "") String email, HttpServletRequest request) {
        return userService.retrieve(email, request);
    }

    @ApiOperation("重置密码")
    @PutMapping("/resetPwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "UUID", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pwd", value = "新密码", required = true, dataType = "String", paramType = "form")
    })
    public Ret resetPwd(@RequestParam String uuid, @RequestParam String pwd) {
        return userService.resetPwd(uuid, pwd);
    }

}
