package com.abc.cx.controller;

import com.abc.cx.annotation.Log;
import com.abc.cx.domain.Param;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.service.IParamService;
import com.abc.cx.service.impl.ParamServiceImpl;
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
 * @Decription: 参数管理
 * @Date: 10:58 2021/11/7
 **/
@Api(tags = "参数管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/param")
public class ParamController {

    private IParamService paramService;

    @Autowired
    public void setParamServiceImpl(ParamServiceImpl paramService) {
        this.paramService = paramService;
    }

    @ApiOperation("获取参数分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "参数名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "key", value = "参数键名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "Integer", paramType = "query"),
    })
    @GetMapping(value = "/pages")

    public Ret list(@RequestParam(defaultValue = "") String name,
                    @RequestParam(defaultValue = "") String key,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer pageSize) {
        return paramService.pageParams(name, key, page, pageSize);
    }

    @ApiOperation("获取参数详情")
    @ApiImplicitParam(name = "id", value = "参数id", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/{id}")
    public Ret get(@PathVariable Long id) {
        return paramService.getParam(id);
    }

    @Log(title = "参数管理", operateType = OperateTypeEnum.ADD)
    @ApiOperation("增加参数")
    @ApiImplicitParam(name = "param", value = "参数对象", required = true, dataType = "Param", paramType = "body")
    @PostMapping
    public Ret add(@RequestBody Param param) {
        return paramService.insertParam(param);
    }

    @Log(title = "参数管理", operateType = OperateTypeEnum.UPDATE)
    @ApiOperation("修改参数")
    @ApiImplicitParam(name = "Param", value = "参数对象", required = true, dataType = "Param", paramType = "body")
    @PutMapping
    public Ret update(@RequestBody Param param) {
        return paramService.updateParam(param);
    }

    @Log(title = "参数管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("删除参数")
    @ApiImplicitParam(name = "id", value = "参数id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public Ret delete(@PathVariable Long id) {
        return paramService.deleteParam(id);
    }

}
