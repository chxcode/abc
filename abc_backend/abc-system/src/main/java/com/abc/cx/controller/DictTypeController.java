package com.abc.cx.controller;

import com.abc.cx.annotation.Log;
import com.abc.cx.domain.DictType;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.service.IDictTypeService;
import com.abc.cx.service.impl.DictTypeServiceImpl;
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
 * @Decription: 字典类型管理
 * @Date: 10:38 2021/11/7
 **/
@Api(tags = "字典类型管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/dictType")
public class DictTypeController {

    private IDictTypeService dictTypeService;

    @Autowired
    public void setDictTypeServiceImpl(DictTypeServiceImpl dictTypeService) {
        this.dictTypeService = dictTypeService;
    }

    @ApiOperation("获取字典类型列表")
    @GetMapping
    public Ret listAll() {
        return dictTypeService.listDictTypes();
    }

    @ApiOperation("获取字典类型分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "字典名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "字典类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "Integer", paramType = "query"),
    })
    @GetMapping(value = "/pages")
    public Ret list(@RequestParam(defaultValue = "") String name,
                    @RequestParam(defaultValue = "") String type,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer pageSize) {
        return dictTypeService.pageDictTypes(name, type, page, pageSize);
    }

    @ApiOperation("获取字典类型详情")
    @ApiImplicitParam(name = "id", value = "字典类型id", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/{id}")
    public Ret get(@PathVariable Long id) {
        return dictTypeService.getDictType(id);
    }

    @Log(title = "字典类型管理", operateType = OperateTypeEnum.ADD)
    @ApiOperation("增加字典类型")
    @ApiImplicitParam(name = "dictType", value = "字典类型对象", required = true, dataType = "DictType", paramType = "body")
    @PostMapping
    public Ret add(@RequestBody DictType dictType) {
        return dictTypeService.insertDictType(dictType);
    }

    @Log(title = "字典类型管理", operateType = OperateTypeEnum.UPDATE)
    @ApiOperation("修改字典类型")
    @ApiImplicitParam(name = "dictType", value = "字典类型对象", required = true, dataType = "DictType", paramType = "body")
    @PutMapping
    public Ret update(@RequestBody DictType dictType) {
        return dictTypeService.updateDictType(dictType);
    }

    @Log(title = "字典类型管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("删除字典类型")
    @ApiImplicitParam(name = "id", value = "字典类型id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public Ret delete(@PathVariable Long id) {
        return dictTypeService.deleteDictType(id);
    }

}
