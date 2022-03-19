package com.abc.cx.controller;

import com.abc.cx.annotation.Log;
import com.abc.cx.domain.Dept;
import com.abc.cx.enums.EnableStatusEnum;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.service.IDeptService;
import com.abc.cx.service.impl.DeptServiceImpl;
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
 * @Decription: 部门管理
 * @Date: 10:29 2021/11/7
 **/
@Api(tags = "部门管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/dept")
public class DeptController {

    private IDeptService deptService;

    @Autowired
    public void setDeptServiceImpl(DeptServiceImpl deptService) {
        this.deptService = deptService;
    }

    @ApiOperation("获取用户所属角色的部门列表")
    @GetMapping(value = "/role/list")
    public Ret roleTreeData() {
        return deptService.getRoleDeptList();
    }

    @ApiOperation("获取部门列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptName", value = "部门名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String", paramType = "query"),
    })
    @GetMapping
    public Ret listAll(@RequestParam(defaultValue = "") String name,
                       @RequestParam(defaultValue = "") EnableStatusEnum status,
                       @RequestParam(defaultValue = "") String beginDate,
                       @RequestParam(defaultValue = "") String endDate) {
        return deptService.listDepts(name, status, beginDate, endDate);
    }

    @ApiOperation("获取部门详情")
    @ApiImplicitParam(name = "id", value = "部门id", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/{id}")
    public Ret get(@PathVariable Long id) {
        return deptService.getDept(id);
    }

    @Log(title = "部门管理", operateType = OperateTypeEnum.ADD)
    @ApiOperation("增加部门")
    @ApiImplicitParam(name = "dept", value = "部门对象", required = true, dataType = "Dept", paramType = "body")
    @PostMapping
    public Ret add(@RequestBody Dept dept) {
        return deptService.insertDept(dept);
    }

    @Log(title = "部门管理", operateType = OperateTypeEnum.UPDATE)
    @ApiOperation("修改部门")
    @ApiImplicitParam(name = "dept", value = "部门对象", required = true, dataType = "Dept", paramType = "body")
    @PutMapping
    public Ret update(@RequestBody Dept dept) {
        return deptService.updateDept(dept);
    }

    @Log(title = "部门管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("删除部门")
    @ApiImplicitParam(name = "id", value = "部门id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public Ret delete(@PathVariable Long id) {
        return deptService.deleteDept(id);
    }

}
