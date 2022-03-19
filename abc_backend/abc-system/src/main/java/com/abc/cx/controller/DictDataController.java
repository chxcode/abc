package com.abc.cx.controller;

import com.abc.cx.annotation.Log;
import com.abc.cx.domain.DictData;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.service.IDictDataService;
import com.abc.cx.service.impl.DictDataServiceImpl;
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
 * @Decription: 字典数据管理
 * @Date: 10:30 2021/11/7
 **/
@Api(tags = "字典数据管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/dictData")
public class DictDataController {

    private IDictDataService dictDataService;

    @Autowired
    public void setDictDataServiceImpl(DictDataServiceImpl dictDataService) {
        this.dictDataService = dictDataService;
    }

    @ApiOperation("获取字典数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "字典类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "label", value = "字典标签", dataType = "String", paramType = "query"),
    })
    @GetMapping
    public Ret list(@RequestParam(defaultValue = "") String type,
                    @RequestParam(defaultValue = "") String label) {
        return dictDataService.listDictData(type, label);
    }

    @ApiOperation("根据字典名称获取字典数据分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "字典名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "Integer", paramType = "query"),
    })
    @GetMapping(value = "/pages")
    public Ret listByDictName(@RequestParam(defaultValue = "") String name,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return dictDataService.pageDictDatas(name, page, pageSize);
    }

    @ApiOperation("获取字典数据详情")
    @ApiImplicitParam(name = "id", value = "字典数据id", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/{id}")
    public Ret get(@PathVariable Long id) {
        return dictDataService.getDictData(id);
    }

    @Log(title = "字典数据管理", operateType = OperateTypeEnum.ADD)
    @ApiOperation("增加字典数据")
    @ApiImplicitParam(name = "dictData", value = "字典数据对象", required = true, dataType = "DictData", paramType = "body")
    @PostMapping
    public Ret add(@RequestBody DictData dictData) {
        return dictDataService.insertDictData(dictData);
    }

    @Log(title = "字典数据管理", operateType = OperateTypeEnum.UPDATE)
    @ApiOperation("修改字典数据")
    @ApiImplicitParam(name = "dictData", value = "字典数据对象", required = true, dataType = "DictData", paramType = "body")
    @PutMapping
    public Ret update(@RequestBody DictData dictData) {
        return dictDataService.updateDictData(dictData);
    }

    @Log(title = "字典数据管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("删除字典数据")
    @ApiImplicitParam(name = "id", value = "字典数据id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public Ret delete(@PathVariable Long id) {
        return dictDataService.deleteDictData(id);
    }

}
