package com.abc.cx.controller;

import com.abc.cx.annotation.Log;
import com.abc.cx.enums.OperateStatusEnum;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.service.IOperateLogService;
import com.abc.cx.service.impl.OperateLogServiceImpl;
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
 * @Decription: 操作日志管理
 * @Date: 10:29 2021/11/7
 **/
@Api(tags = "操作日志管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/operLog")
public class OperateLogController {

    private IOperateLogService operateLogService;

    @Autowired
    public void setOperateLogServiceImpl(OperateLogServiceImpl operateLogService) {
        this.operateLogService = operateLogService;
    }

    @ApiOperation("获取操作日志分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "operName", value = "操作人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "操作模块", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "操作类别", dataType = "OperateType", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "操作状态", dataType = "OperateStatus", paramType = "query"),
            @ApiImplicitParam(name = "beginDate", value = "起始日期", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "/pages")
    public Ret list(@RequestParam(defaultValue = "") String title,
                    @RequestParam(defaultValue = "") OperateTypeEnum type,
                    @RequestParam(defaultValue = "") OperateStatusEnum status,
                    @RequestParam(defaultValue = "") String beginDate,
                    @RequestParam(defaultValue = "") String endDate,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer pageSize) {
        return operateLogService.pageOperateLogs(title, type, status, beginDate, endDate, page, pageSize);
    }

    @ApiOperation("获取操作日志详情")
    @ApiImplicitParam(name = "id", value = "部门id", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/{id}")
    public Ret get(@PathVariable Long id) {
        return operateLogService.getOperateLog(id);
    }

    @Log(title = "操作日志管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("删除操作日志")
    @ApiImplicitParam(name = "id", value = "操作日志id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public Ret delete(@PathVariable Long id) {
        return operateLogService.deleteOperateLog(id);
    }

}
