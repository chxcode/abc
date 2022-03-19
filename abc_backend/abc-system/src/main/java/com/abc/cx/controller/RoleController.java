package com.abc.cx.controller;

import com.abc.cx.annotation.Log;
import com.abc.cx.domain.Role;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.service.IRoleService;
import com.abc.cx.service.impl.RoleServiceImpl;
import com.abc.cx.vo.Ret;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: ChangXuan
 * @Decription: 角色管理
 * @Date: 11:15 2021/11/7
 **/
@Api(tags = "角色管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/role")
public class RoleController {

    private IRoleService roleService;

    @Autowired
    public void setRoleServiceImpl(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @ApiOperation("获取角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "角色状态", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleKey", value = "角色权限", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleSort", value = "角色排序", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "beginDate", value = "开始日期", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String", paramType = "query"),
    })
    @GetMapping
    public Ret list(@RequestParam(defaultValue = "") String roleName,
                    @RequestParam(defaultValue = "") EnableStatusEnum status,
                    @RequestParam(defaultValue = "") String roleKey,
                    @RequestParam(defaultValue = "") String roleSort,
                    @RequestParam(defaultValue = "") String beginDate,
                    @RequestParam(defaultValue = "") String endDate) {
        return roleService.listRoles(roleName, status, roleKey, roleSort, beginDate, endDate);
    }

    @ApiOperation("获取角色详情")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/{id}")
    public Ret get(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @Log(title = "角色管理", operateType = OperateTypeEnum.ADD)
    @ApiOperation("增加角色")
    @ApiImplicitParam(name = "role", value = "角色对象", required = true, dataType = "Role", paramType = "body")
    @PostMapping
    public Ret add(@RequestBody Role role) {
        return roleService.insertRole(role);
    }

    @Log(title = "角色管理", operateType = OperateTypeEnum.UPDATE)
    @ApiOperation("修改角色")
    @ApiImplicitParam(name = "role", value = "角色对象", required = true, dataType = "Role", paramType = "body")
    @PutMapping
    public Ret update(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @Log(title = "角色管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("删除角色")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public Ret delete(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @Log(title = "角色管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("批量删除角色")
    @ApiImplicitParam(name = "ids", value = "数据id集合", required = true, dataType = "List", paramType = "query")
    @DeleteMapping("/batch")
    public Ret deleteBatch(@RequestParam List<Long> ids) {
        return roleService.deleteRoles(ids);
    }

}
