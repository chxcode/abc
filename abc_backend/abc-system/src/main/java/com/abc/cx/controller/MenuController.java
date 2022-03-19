package com.abc.cx.controller;

import com.abc.cx.annotation.Log;
import com.abc.cx.domain.Menu;
import com.abc.cx.enums.OperateTypeEnum;
import com.abc.cx.enums.VisibleTypeEnum;
import com.abc.cx.service.IMenuService;
import com.abc.cx.service.impl.MenuServiceImpl;
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
 * @Decription: 菜单管理
 * @Date: 10:49 2021/11/7
 **/
@Api(tags = "菜单管理")
@Slf4j
@RestController
@RequestMapping(value = "/api/systems/menu")
public class MenuController {

    private IMenuService menuService;

    @Autowired
    public void setMenuServiceImpl(MenuServiceImpl menuService) {
        this.menuService = menuService;
    }

    @ApiOperation("获取菜单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "菜单名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "菜单类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "visible", value = "是否显示", dataType = "VisibleType", paramType = "query"),
    })
    @GetMapping("/pages")
    public Ret list(@RequestParam(defaultValue = "") String name,
                    @RequestParam(defaultValue = "") String type,
                    @RequestParam(defaultValue = "") VisibleTypeEnum visible) {
        return menuService.listMenus(name, type, visible);
    }

    @ApiOperation("获取主菜单列表")
    @GetMapping(value = "/main")
    public Ret listMain() {
        return menuService.getMainMenuList();
    }

    @ApiOperation("获取子菜单列表")
    @ApiImplicitParam(name = "id", value = "主菜单id", dataType = "Long", paramType = "query")
    @GetMapping(value = "/main/{id}/child")
    public Ret listSideMenu(@PathVariable Long id) {
        return menuService.getSideMenuListByMain(id);
    }

    @ApiOperation("获取页面按钮列表")
    @ApiImplicitParam(name = "id", value = "页面Id", dataType = "Long", paramType = "query")
    @GetMapping(value = "/child/{id}/button")
    public Ret listButtonByPage(@PathVariable Long id) {
        return menuService.getButtonListByPage(id);
    }


    @ApiOperation("获取菜单详情")
    @ApiImplicitParam(name = "id", value = "菜单id", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/get/{id}")
    public Ret get(@PathVariable Long id) {
        return menuService.getMenu(id);
    }

    @Log(title = "菜单管理", operateType = OperateTypeEnum.ADD)
    @ApiOperation("增加菜单")
    @ApiImplicitParam(name = "menu", value = "菜单对象", required = true, dataType = "Menu", paramType = "body")
    @PostMapping
    public Ret add(@RequestBody Menu menu) {
        return menuService.insertMenu(menu);
    }

    @Log(title = "菜单管理", operateType = OperateTypeEnum.UPDATE)
    @ApiOperation("修改菜单")
    @ApiImplicitParam(name = "dept", value = "菜单对象", required = true, dataType = "Dept", paramType = "body")
    @PutMapping
    public Ret update(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    @Log(title = "菜单管理", operateType = OperateTypeEnum.DELETE)
    @ApiOperation("删除菜单")
    @ApiImplicitParam(name = "id", value = "菜单id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public Ret delete(@PathVariable Long id) {
        return menuService.deleteMenu(id);
    }

}
